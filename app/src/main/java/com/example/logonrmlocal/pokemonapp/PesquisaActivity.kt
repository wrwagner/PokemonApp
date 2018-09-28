package com.example.logonrmlocal.pokemonapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.logonrmlocal.pokemonapp.API.PokemonAPI
import com.example.logonrmlocal.pokemonapp.model.Pokemon
import kotlinx.android.synthetic.main.activity_pesquisa.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient



class PesquisaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesquisa)

        btPesquisar.setOnClickListener{
            val okhttp = OkHttpClient.Builder()
                    .addNetworkInterceptor(StethoInterceptor())
                    .build();

            val retrofit = Retrofit.Builder()
                    .baseUrl("https://pokeapi.co/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttp)
                    .build()

            val api = retrofit.create(PokemonAPI::class.java)

            api.getPokemonBy(etNumero.text.toString().toInt())
                    .enqueue(object : Callback<Pokemon>{
                        override fun onResponse(call: Call<Pokemon>?, response: Response<Pokemon>?) {
                            if (response?.isSuccessful == true){
                                val pokemon = response.body()
                                tvPokemon.text = pokemon?.name
                                Picasso.get()
                                        .load(pokemon?.sprites?.frontDefault)
                                        .placeholder(R.drawable.loading)
                                        .error(R.drawable.notfound)
                                        .into(ivPokemon)
                            } else {
                                Toast.makeText(this@PesquisaActivity,
                                        "Deu ruim",
                                        Toast.LENGTH_LONG).show()
                                Picasso.get()
                                        .load(R.drawable.notfound)
                                        .placeholder(R.drawable.loading)
                                        .error(R.drawable.notfound)
                                        .into(ivPokemon)
                            }
                        }

                        override fun onFailure(call: Call<Pokemon>?, t: Throwable?) {
                            Toast.makeText(this@PesquisaActivity,
                                    t?.message,
                                    Toast.LENGTH_LONG).show()

                        }
                    })
        }
    }
}
