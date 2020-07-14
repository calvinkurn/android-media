package com.tokopedia.fakeresponse.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.tokopedia.fakeresponse.App

open class BaseActivity : AppCompatActivity() {
    val gson = GsonBuilder().setPrettyPrinting().create()
    val jsonParser = JsonParser()

    open fun getLayout() = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.setContext(application)
    }

    @Throws(Exception::class)
    fun setPrettyText(text: String?, editText: EditText) {
        if (!text.isNullOrEmpty()) {
            val formattedText = gson.toJson(JsonParser().parse(text))
            editText.setText(formattedText)
        }
    }

    fun sendData(string: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, string)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}