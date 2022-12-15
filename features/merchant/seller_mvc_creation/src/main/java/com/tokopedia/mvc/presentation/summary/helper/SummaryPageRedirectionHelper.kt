package com.tokopedia.mvc.presentation.summary.helper

import android.app.Activity
import android.content.Intent
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.presentation.product.list.ProductListActivity

class SummaryPageRedirectionHelper(private val listener: SummaryPageResultListener) {
    companion object {
        private const val REQUEST_CODE_ADD_PRODUCT = 100
        private const val REQUEST_CODE_VIEW_PRODUCT = 101
    }

    interface SummaryPageResultListener {
        fun onAddProductResult()
        fun onViewProductResult()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_CODE_ADD_PRODUCT -> listener.onAddProductResult()
            REQUEST_CODE_VIEW_PRODUCT -> listener.onViewProductResult()
        }
    }

    fun redirectToAddProductPage(activity: Activity, configuration: VoucherConfiguration) {
        val intent = ProductListActivity.buildIntentForVoucherSummaryPage(
            context = activity,
            pageMode = PageMode.CREATE,
            showCtaChangeProductOnToolbar = false,
            voucherConfiguration = configuration,
            selectedProducts = emptyList()
        )
        activity.startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
    }

    fun redirectToViewProductPage(activity: Activity, configuration: VoucherConfiguration, selectedProducts: List<SelectedProduct>) {
        val intent = ProductListActivity.buildIntentForVoucherSummaryPage(
            context = activity,
            pageMode = PageMode.EDIT,
            showCtaChangeProductOnToolbar = true,
            voucherConfiguration = configuration,
            selectedProducts = selectedProducts
        )
        activity.startActivityForResult(intent, REQUEST_CODE_VIEW_PRODUCT)
    }
}
