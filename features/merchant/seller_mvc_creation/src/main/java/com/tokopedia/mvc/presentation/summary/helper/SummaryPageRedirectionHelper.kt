package com.tokopedia.mvc.presentation.summary.helper

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.presentation.creation.step1.VoucherTypeActivity
import com.tokopedia.mvc.presentation.creation.step2.VoucherInformationActivity
import com.tokopedia.mvc.presentation.creation.step3.VoucherSettingActivity
import com.tokopedia.mvc.presentation.product.list.ProductListActivity

class SummaryPageRedirectionHelper(private val listener: SummaryPageResultListener) {
    companion object {
        private const val REQUEST_CODE_ADD_PRODUCT = 100
        private const val REQUEST_CODE_VIEW_PRODUCT = 101
        private const val REQUEST_CODE_CHANGE_COUPON_TYPE = 102
    }

    interface SummaryPageResultListener {
        fun onAddProductResult()
        fun onViewProductResult()
        fun onVoucherTypePageResult()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        when(requestCode) {
            REQUEST_CODE_ADD_PRODUCT -> listener.onAddProductResult()
            REQUEST_CODE_VIEW_PRODUCT -> listener.onViewProductResult()
            REQUEST_CODE_CHANGE_COUPON_TYPE -> listener.onVoucherTypePageResult()
        }
    }

    fun redirectToAddProductPage(
        fragment: Fragment,
        configuration: VoucherConfiguration,
        selectedProducts: List<SelectedProduct>,
        selectedWarehouseId: Long
    ) {
        val context = fragment.context ?: return
        val intent = ProductListActivity.buildIntentForVoucherSummaryPage(
            context = context,
            pageMode = PageMode.CREATE,
            showCtaChangeProductOnToolbar = false,
            voucherConfiguration = configuration,
            selectedProducts = selectedProducts,
            selectedWarehouseId = selectedWarehouseId
        )
        fragment.startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
    }

    fun redirectToViewProductPage(
        fragment: Fragment,
        configuration: VoucherConfiguration,
        selectedProducts: List<SelectedProduct>,
        selectedWarehouseId: Long
    ) {
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

    fun redirectToVoucherTypePage(
        fragment: Fragment,
        configuration: VoucherConfiguration
    ) {
        val context = fragment.context ?: return
        val intent = VoucherTypeActivity.buildEditModeIntent(context, configuration)
        fragment.startActivityForResult(intent, REQUEST_CODE_CHANGE_COUPON_TYPE)
    }

    fun redirectToCouponInfoPage(
        fragment: Fragment,
        configuration: VoucherConfiguration,
        isAdding: Boolean
    ) {
        val context = fragment.context ?: return
        if (isAdding) {
            VoucherInformationActivity.buildCreateModeIntent(context, configuration)
        } else {
            val intent = VoucherInformationActivity.buildEditModeIntent(context, configuration)
            fragment.startActivity(intent)
        }
    }

    fun redirectToCouponConfigurationPage(
        fragment: Fragment,
        configuration: VoucherConfiguration,
        isAdding: Boolean
    ) {
        val context = fragment.context ?: return
        if (isAdding) {
            VoucherSettingActivity.buildCreateModeIntent(context, configuration)
        } else {
            val intent = VoucherSettingActivity.buildEditModeIntent(context, configuration)
            fragment.startActivity(intent)
        }
    }
}
