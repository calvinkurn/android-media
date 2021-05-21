package com.tokopedia.minicart.example

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.minicart.R
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener

class ExampleActivity : AppCompatActivity(), MiniCartWidgetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        val miniCartWidget = findViewById<MiniCartWidget>(R.id.mini_cart_widget)
        miniCartWidget?.initialize(listOf("1"), this, this)
//        miniCartWidget?.updateData(MiniCartWidgetData(totalProductCount = 10, totalProductPrice = 1000000))
        miniCartWidget?.updateData()
    }

    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
        Toast.makeText(this, this.javaClass.name + "  - onCartItemsUpdated", Toast.LENGTH_SHORT).show()
    }

}