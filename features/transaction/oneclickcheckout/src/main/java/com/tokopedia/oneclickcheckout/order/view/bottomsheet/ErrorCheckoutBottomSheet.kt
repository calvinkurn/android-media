package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.view.LayoutInflater
import com.tokopedia.oneclickcheckout.databinding.BottomSheetErrorCheckoutBinding
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccErrorData
import com.tokopedia.unifycomponents.BottomSheetUnify

class ErrorCheckoutBottomSheet {

    var listener: Listener? = null

    fun show(view: OrderSummaryPageFragment, error: CheckoutOccErrorData, listener: Listener) {
        this.listener = listener
        view.parentFragmentManager.let {
            BottomSheetUnify().apply {
                showCloseIcon = true
                showHeader = true
                setTitle(if (error.code == ERROR_CODE_PRODUCT_STOCK_EMPTY) TITLE_PRODUCT_STOCK_EMPTY else TITLE_SHOP_CLOSED)
                val binding = BottomSheetErrorCheckoutBinding.inflate(LayoutInflater.from(view.context))
                setupView(binding, error)
                setChild(binding.root)
                show(it, null)
            }
        }
    }

    private fun setupView(binding: BottomSheetErrorCheckoutBinding, error: CheckoutOccErrorData) {
        binding.esCheckout.setImageUrl(if (error.code == ERROR_CODE_PRODUCT_STOCK_EMPTY) IMAGE_PRODUCT_STOCK_EMPTY else IMAGE_SHOP_CLOSED)
        binding.esCheckout.setDescription(error.message)
        binding.esCheckout.setPrimaryCTAText(ACTION_MESSAGE)
        binding.esCheckout.setPrimaryCTAClickListener {
            listener?.onClickSimilarProduct(error.code)
        }
    }

    interface Listener {
        fun onClickSimilarProduct(errorCode: String)
    }

    companion object {
        const val ERROR_CODE_PRODUCT_STOCK_EMPTY = "511"
        const val ERROR_CODE_PRODUCT_ERROR = "510"
        const val ERROR_CODE_SHOP_CLOSED = "520"

        const val TITLE_PRODUCT_STOCK_EMPTY = "Yaah, barangnya habis"
        const val TITLE_SHOP_CLOSED = "Yaah, tokonya baru aja tutup"

        const val ACTION_MESSAGE = "Cari Barang Serupa"

        const val IMAGE_PRODUCT_STOCK_EMPTY = "https://ecs7.tokopedia.net/android/others/beli_langsung_stok_habis.png"
        const val IMAGE_SHOP_CLOSED = "https://ecs7.tokopedia.net/android/others/beli_langsung_toko_tutup.png"
    }
}