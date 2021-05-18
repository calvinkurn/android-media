package com.tokopedia.minicart.example

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.minicart.R
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.ui.widget.MiniCartWidget
import com.tokopedia.minicart.ui.widget.MiniCartWidgetListener

class ExampleActivity : AppCompatActivity(), MiniCartWidgetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        Log.d("MiniCart", "LaunchActivityExample")

        val miniCartWidget = findViewById<MiniCartWidget>(R.id.mini_cart_widget)
        miniCartWidget?.setup(this, this)
//        miniCartWidget?.updateData(MiniCartWidgetData(totalProductCount = 10, totalProductPrice = 1000000))
        miniCartWidget?.updateData()
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        Toast.makeText(this, this.javaClass.name + "  - onCartItemsUpdated", Toast.LENGTH_SHORT).show()
    }

}