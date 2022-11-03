package com.example.api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.net.URL
import android.util.Log
import android.widget.TextView
import java.lang.RuntimeException
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val API_URL = "https://www.mercadobitcoin.net/api/BTC/ticker/"
    var cotacaoBitcoin: Double = 0.0
    private val TAG = "Pedro"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.v(TAG, "Log de verbose")
        Log.d(TAG, "Log de debug")
        Log.i(TAG, "Log de info")
        Log.w(TAG, "Log de alerta")
        Log.e(TAG, "Log de erro", RuntimeException("teste de erro"))

        var btnCalcular = findViewById<TextView>(R.id.btnCalcular)
        buscarCotacao()
        btnCalcular.setOnClickListener{
            calcular()
        }
    }

    fun buscarCotacao(){
        GlobalScope.async (Dispatchers.Default) {
            val resposta = URL(API_URL).readText()
            cotacaoBitcoin = JSONObject(resposta).getJSONObject("ticker").getDouble("last")
            val f = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            val cotacaoFormatada = f.format(cotacaoBitcoin)
            var txtCotacao = findViewById<TextView>(R.id.txtCotacao)
            txtCotacao.setText("$cotacaoFormatada")

            withContext(Main){

            }
        }
    }

    fun calcular(){
        var txtValor = findViewById<TextView>(R.id.txtValor)
        var txtQtdBitcoins = findViewById<TextView>(R.id.textQtdBitcoins)

        if (txtValor.text.isEmpty()){
            txtValor.error = "Preencha um valor"
            return
        } else {
            val valorDigitado = txtValor.text.toString().replace(",", ".").toDouble()
            val resultado = if(cotacaoBitcoin > 0) valorDigitado / cotacaoBitcoin else 0.0
            txtQtdBitcoins.text = "%.8f".format(resultado)
        }
    }
}