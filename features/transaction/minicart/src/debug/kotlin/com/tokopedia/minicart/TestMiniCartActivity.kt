package com.tokopedia.minicart

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartNewWidget
import com.tokopedia.minicart.common.widget.MiniCartNewWidgetListener

class TestMiniCartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_mini_cart)

        findViewById<MiniCartNewWidget>(R.id.test_mini_cart).apply {
            initialize(
                MiniCartNewWidget.MiniCartNewWidgetConfig(
                    showTopShadow = false,
                    showChevron = true
                ),
                this@TestMiniCartActivity, this@TestMiniCartActivity,
                object : MiniCartNewWidgetListener {
                    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
                        Log.i("qwertyuiop", miniCartSimplifiedData.toString())
                    }

                    override fun onChevronClickListener() {
                        Toast.makeText(this@TestMiniCartActivity, "testing", Toast.LENGTH_SHORT).show()
                    }

                    override fun getFragmentManager(): FragmentManager? {
                        return context?.let { this@TestMiniCartActivity.supportFragmentManager }
                    }
                }
            )
            updateData(listOf("480552"))
        }
    }
}
