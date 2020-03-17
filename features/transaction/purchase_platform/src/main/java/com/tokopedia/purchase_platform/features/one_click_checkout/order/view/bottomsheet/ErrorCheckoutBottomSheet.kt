package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet

import android.view.View
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccGlobalEvent
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_error_checkout.view.*

class ErrorCheckoutBottomSheet {

    var listener: Listener? = null

    fun show(view: OrderSummaryPageFragment, error: OccGlobalEvent.CheckoutError, listener: Listener) {
        this.listener = listener
        view.fragmentManager?.let {
            BottomSheetUnify().apply {
                showCloseIcon = true
                showHeader = true
                setTitle(if (error.error.code == "510") "Yaah, barangnya habis" else "Yaah, tokonya baru aja tutup")
                val child = View.inflate(view.context, R.layout.bottom_sheet_error_checkout, null)
//                view.view?.height?.div(2)?.let { height ->
//                    customPeekHeight = height
//                }
                setupView(child, error)
                setChild(child)
                show(it, null)
            }
        }
    }

    private fun setupView(child: View, error: OccGlobalEvent.CheckoutError) {
        val esCheckout = child.es_checkout
        esCheckout.setImageUrl(error.error.imageUrl)
        esCheckout.setDescription(error.error.message)
        esCheckout.setPrimaryCTAText("Cari Barang Serupa")
        esCheckout.setPrimaryCTAClickListener {
            listener?.onClickSimilarProduct()
        }
    }

    interface Listener {

        fun onClickSimilarProduct()
    }

    companion object {
        const val ERROR_CODE_PRODUCT_STOCK_EMPTY = "510"
        const val ERROR_CODE_SHOP_CLOSED = "520"
    }
}