package com.tokopedia.mvc.presentation.summary.helper

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
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
        if (resultCode != Activity.RESULT_OK) return
        when(requestCode) {
            REQUEST_CODE_ADD_PRODUCT -> listener.onAddProductResult()
            REQUEST_CODE_VIEW_PRODUCT -> listener.onViewProductResult()
        }
    }

    fun redirectToAddProductPage(fragment: Fragment, configuration: VoucherConfiguration, selectedProducts: List<SelectedProduct>) {
        val context = fragment.context ?: return
    fun redirectToAddProductPage(
        activity: Activity,
        configuration: VoucherConfiguration,
        selectedWarehouseId: Long
    ) {
        val intent = ProductListActivity.buildIntentForVoucherSummaryPage(
            context = context,
            pageMode = PageMode.CREATE,
            showCtaChangeProductOnToolbar = false,
            voucherConfiguration = configuration,
            selectedProducts = selectedProducts
            selectedProducts = emptyList(),
            selectedWarehouseId = selectedWarehouseId
        )
        fragment.startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
    }

    fun redirectToViewProductPage(
        activity: Activity,
        configuration: VoucherConfiguration,
        selectedProducts: List<SelectedProduct>,
        selectedWarehouseId: Long
    ) {
    fun redirectToViewProductPage(fragment: Fragment, configuration: VoucherConfiguration, selectedProducts: List<SelectedProduct>) {
        val context = fragment.context ?: return
        val intent = ProductListActivity.buildIntentForVoucherSummaryPage(
            context = context,
            pageMode = PageMode.EDIT,
            showCtaChangeProductOnToolbar = true,
            voucherConfiguration = configuration,
            selectedProducts = selectedProducts,
            selectedWarehouseId = selectedWarehouseId
        )
        fragment.startActivityForResult(intent, REQUEST_CODE_VIEW_PRODUCT)
    }
}
