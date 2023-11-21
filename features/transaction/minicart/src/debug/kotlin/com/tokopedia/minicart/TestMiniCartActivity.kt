package com.tokopedia.minicart

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.minicart.cartlist.MiniCartListNewBottomSheet
import com.tokopedia.minicart.cartlist.MiniCartListNewBottomSheetListener
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.MiniCartNewWidget
import com.tokopedia.minicart.common.widget.MiniCartNewWidgetListener
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TestMiniCartActivity : AppCompatActivity() {

    private var miniCartData: MiniCartSimplifiedData = MiniCartSimplifiedData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_mini_cart)

        findViewById<MiniCartNewWidget>(R.id.test_mini_cart).apply {
            initialize(
                MiniCartNewWidget.MiniCartNewWidgetConfig(
                    showTopShadow = false,
                    showChevron = true,
                    showOriginalTotalPrice = true,
                    overridePrimaryButtonWording = "apa ini",
                    additionalButton = getIconUnifyDrawable(context, IconUnify.CHAT, ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500))
                ),
                this@TestMiniCartActivity,
                this@TestMiniCartActivity,
                getMiniCartNewWidgetListener()
            )
            updateData(listOf("480552"))
        }
    }

    private fun getMiniCartNewWidgetListener(): MiniCartNewWidgetListener {
        return object : MiniCartNewWidgetListener {
            override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
                Log.i("qwertyuiop", miniCartSimplifiedData.toString())
                miniCartData = miniCartSimplifiedData
            }

            override fun onChevronClickListener() {
                Toast.makeText(this@TestMiniCartActivity, "testing", Toast.LENGTH_SHORT)
                    .show()
                //                        ShoppingSummaryBottomSheet().show(miniCartData.shoppingSummaryBottomSheetData, this@TestMiniCartActivity.supportFragmentManager, this@TestMiniCartActivity)
                showMiniCartListNewBottomSheet()
            }

            override fun getFragmentManager(): FragmentManager? {
                return this@TestMiniCartActivity.supportFragmentManager
            }
        }
    }

    private fun showMiniCartListNewBottomSheet() {
        MiniCartListNewBottomSheet().show(
            this@TestMiniCartActivity,
            this@TestMiniCartActivity.supportFragmentManager,
            this@TestMiniCartActivity,
            this@TestMiniCartActivity,
            null,
            getMiniCartListNewBottomSheetListener()
        )
    }

    private fun getMiniCartListNewBottomSheetListener(): MiniCartListNewBottomSheetListener {
        return object : MiniCartListNewBottomSheetListener {
            override fun onMiniCartListBottomSheetDismissed() {
                findViewById<MiniCartNewWidget>(R.id.test_mini_cart).updateData(listOf("480552"))
            }

            override fun onRetryOpenMiniCartListBottomSheet() {
                showMiniCartListNewBottomSheet()
            }
        }
    }
}
