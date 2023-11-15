package com.tokopedia.minicart

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.tokopedia.minicart.cartlist.MiniCartListBottomSheetListener
import com.tokopedia.minicart.cartlist.MiniCartListNewBottomSheet
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.widget.GlobalEvent
import com.tokopedia.minicart.common.widget.MiniCartNewWidget
import com.tokopedia.minicart.common.widget.MiniCartNewWidgetListener

class TestMiniCartActivity : AppCompatActivity() {

    private var miniCartData: MiniCartSimplifiedData = MiniCartSimplifiedData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_mini_cart)

        findViewById<MiniCartNewWidget>(R.id.test_mini_cart).apply {
            initialize(
                MiniCartNewWidget.MiniCartNewWidgetConfig(
                    showTopShadow = false,
                    showChevron = true
                ),
                this@TestMiniCartActivity,
                this@TestMiniCartActivity,
                object : MiniCartNewWidgetListener {
                    override fun onCartItemsUpdated(miniCartSimplifiedData: MiniCartSimplifiedData) {
                        Log.i("qwertyuiop", miniCartSimplifiedData.toString())
                        miniCartData = miniCartSimplifiedData
                    }

                    override fun onChevronClickListener() {
                        Toast.makeText(this@TestMiniCartActivity, "testing", Toast.LENGTH_SHORT)
                            .show()
//                        ShoppingSummaryBottomSheet().show(miniCartData.shoppingSummaryBottomSheetData, this@TestMiniCartActivity.supportFragmentManager, this@TestMiniCartActivity)
                        MiniCartListNewBottomSheet().show(
                            this@TestMiniCartActivity,
                            this@TestMiniCartActivity.supportFragmentManager,
                            this@TestMiniCartActivity,
                            this@TestMiniCartActivity,
                            null,
                            object : MiniCartListBottomSheetListener {
                                override fun onMiniCartListBottomSheetDismissed() {
                                    updateData(listOf("480552"))
                                }

                                override fun onBottomSheetSuccessGoToCheckout() {
                                }

                                override fun onBottomSheetFailedGoToCheckout(
                                    toasterAnchorView: View,
                                    fragmentManager: FragmentManager,
                                    globalEvent: GlobalEvent
                                ) {
                                }

                                override fun showToaster(
                                    view: View?,
                                    message: String,
                                    type: Int,
                                    ctaText: String,
                                    isShowCta: Boolean,
                                    onClickListener: View.OnClickListener?
                                ) {
                                }

                                override fun showProgressLoading() {
                                }

                                override fun hideProgressLoading() {
                                }
                            }
                        )
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
