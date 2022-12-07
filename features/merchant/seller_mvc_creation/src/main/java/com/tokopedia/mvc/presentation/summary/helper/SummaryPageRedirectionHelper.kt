package com.tokopedia.mvc.presentation.summary.helper

import android.app.Activity
import android.content.Intent
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.presentation.product.add.AddProductActivity
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
        val intent = AddProductActivity.buildEditModeIntent(
            activity,
            configuration
        )
        activity.startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
    }

    fun redirectToViewProductPage(activity: Activity, configuration: VoucherConfiguration, selectedProducts: List<SelectedProduct>) {
        val intent = ProductListActivity.buildEditModeIntent(
            activity,
            configuration,
            selectedProducts
        )
        activity.startActivityForResult(intent, REQUEST_CODE_VIEW_PRODUCT)
    }
}
