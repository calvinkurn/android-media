package com.tokopedia.minicart

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.v2.MiniCartV2Widget
import com.tokopedia.minicart.v2.MiniCartV2WidgetListener
import com.tokopedia.minicart.test.R as minicarttestR

class MiniCartTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(minicarttestR.layout.activity_mini_cart_test)

        findViewById<MiniCartV2Widget>(minicarttestR.id.mini_cart_widget)?.apply {
            initialize(
                MiniCartV2Widget.MiniCartV2WidgetConfig(
                    showTopShadow = false
                ),
                object : MiniCartV2WidgetListener() {
                    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
                        Log.i("mini_cart_test", miniCartSimplifiedData.toString())
                    }
                }
            )
        }
    }
}
