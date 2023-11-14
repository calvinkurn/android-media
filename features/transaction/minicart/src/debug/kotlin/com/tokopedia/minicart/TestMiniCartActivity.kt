package com.tokopedia.minicart

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartNewWidget
import com.tokopedia.minicart.common.widget.MiniCartWidgetListener

class TestMiniCartActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_mini_cart)

        findViewById<MiniCartNewWidget>(R.id.test_mini_cart).apply {
            initialize(MiniCartNewWidget.MiniCartNewWidgetConfig(
                showTopShadow = true
            ), this@TestMiniCartActivity, this@TestMiniCartActivity, object : MiniCartWidgetListener {
                override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
                    Log.i("qwertyuiop", miniCartSimplifiedData.toString())
                }
            })
            updateData(listOf("123"))
        }
    }
}
