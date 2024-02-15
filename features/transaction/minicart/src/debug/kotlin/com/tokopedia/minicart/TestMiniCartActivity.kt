package com.tokopedia.minicart

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.minicart.domain.GetMiniCartParam
import com.tokopedia.minicart.v2.MiniCartV2Widget
import com.tokopedia.minicart.v2.MiniCartV2WidgetListener
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TestMiniCartActivity : AppCompatActivity() {

    private var miniCartData: MiniCartSimplifiedData = MiniCartSimplifiedData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_mini_cart)

        findViewById<MiniCartV2Widget>(R.id.test_mini_cart).apply {
            initialize(
                MiniCartV2Widget.MiniCartV2WidgetConfig(
                    showTopShadow = false,
                    showChevron = true,
                    showOriginalTotalPrice = true,
                    overridePrimaryButtonWording = "apa ini",
                    additionalButton = getIconUnifyDrawable(
                        context,
                        IconUnify.CHAT,
                        ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500)
                    )
                ),
                getMiniCartV2WidgetListener()
            )
        }
    }

    override fun onStart() {
        super.onStart()
        findViewById<MiniCartV2Widget>(R.id.test_mini_cart).apply {
            // tokonow
//            refresh(GetMiniCartParam(listOf("480552"), MiniCartSource.MiniCartBottomSheet.value))

            // BMGM
            refresh(
                GetMiniCartParam(
                    listOf("6552061"),
                    MiniCartSource.DiscoOfferPage.value,
                    bmgm = GetMiniCartParam.GetMiniCartBmgmParam(
                        offerIds = listOf(
                            1739
                        ),
                        offerJsonData = "",
                        warehouseIds = listOf(
                            342370
                        )
                    )
                )
            )
        }
    }

    private fun getMiniCartV2WidgetListener(): MiniCartV2WidgetListener {
        return object : MiniCartV2WidgetListener() {
            override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
                Log.i("qwertyuiop", miniCartSimplifiedData.toString())
                miniCartData = miniCartSimplifiedData
            }

            override fun onChevronClickListener() {
                Toast.makeText(this@TestMiniCartActivity, "testing", Toast.LENGTH_SHORT)
                    .show()
                //                        ShoppingSummaryBottomSheet().show(miniCartData.shoppingSummaryBottomSheetData, this@TestMiniCartActivity.supportFragmentManager, this@TestMiniCartActivity)
            }

            override fun getFragmentManager(): FragmentManager? {
                return this@TestMiniCartActivity.supportFragmentManager
            }

            override fun onFailedToLoadMiniCartWidget() {
                Toast.makeText(this@TestMiniCartActivity, "fail load", Toast.LENGTH_SHORT).show()
                super.onFailedToLoadMiniCartWidget()
            }
        }
    }

    companion object {
        const val SHOP_ID_NOW_STAGING = "480552"
        const val SHOP_ID_NOW_PROD = "11515028"
    }
}
