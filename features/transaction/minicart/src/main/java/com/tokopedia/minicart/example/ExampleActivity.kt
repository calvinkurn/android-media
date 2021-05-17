package com.tokopedia.minicart.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tokopedia.minicart.R
import com.tokopedia.totalamount.TotalAmount

class ExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        Log.d("MiniCart", "LaunchActivityExample")

        val totalAmount = findViewById<TotalAmount>(R.id.mini_cart_total_amount)
        totalAmount.let {  }
    }

}