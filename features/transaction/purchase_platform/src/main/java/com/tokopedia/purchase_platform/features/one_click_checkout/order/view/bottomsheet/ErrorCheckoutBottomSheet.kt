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
                setTitle(if (error.error.code == ERROR_CODE_PRODUCT_STOCK_EMPTY) TITLE_PRODUCT_STOCK_EMPTY else TITLE_SHOP_CLOSED)
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
        esCheckout.setImageUrl(if (error.error.code == ERROR_CODE_PRODUCT_STOCK_EMPTY) IMAGE_PRODUCT_STOCK_EMPTY else IMAGE_SHOP_CLOSED)
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
        const val ERROR_CODE_PRODUCT_STOCK_EMPTY = "511"
        const val ERROR_CODE_SHOP_CLOSED = "520"

        const val TITLE_PRODUCT_STOCK_EMPTY = "Yaah, barangnya habis"
        const val TITLE_SHOP_CLOSED = "Yaah, tokonya baru aja tutup"

        const val IMAGE_PRODUCT_STOCK_EMPTY = "https://ecs7.tokopedia.net/android/others/beli_langsung_stok_habis.png"
        const val IMAGE_SHOP_CLOSED = "https://ecs7.tokopedia.net/android/others/beli_langsung_toko_tutup.png"
    }
}