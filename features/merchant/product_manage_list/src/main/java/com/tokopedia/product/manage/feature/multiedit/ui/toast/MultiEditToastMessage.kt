package com.tokopedia.product.manage.feature.multiedit.ui.toast

import android.content.Context
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.*
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus.*

object MultiEditToastMessage {

    private fun getEditByStatusSuccessMessage(context: Context, result: EditByStatus): String {
        val successCount = result.success.count()

        val stringResId = when(result.status) {
            INACTIVE -> R.string.product_manage_edit_products_inactive_success
            DELETED -> R.string.product_manage_delete_products_success
            else -> R.string.product_manage_edit_products_success
        }

        return context.getString(stringResId, successCount)
    }

    private fun getEditEtalaseSuccessMessage(context: Context, result: MultiEditResult): String {
        val successCount = result.success.count()
        val stringResId = R.string.product_manage_edit_products_etalase_success

        return context.getString(stringResId, successCount)
    }

    private fun getEditByStatusRetryMessage(context: Context, result: EditByStatus): String {
        val successCount = result.success.count()
        val failedCount = result.failed.count()

        val stringResId = when(result.status) {
            INACTIVE -> R.string.product_manage_edit_products_inactive_retry
            DELETED -> R.string.product_manage_delete_products_retry
            else -> R.string.product_manage_edit_products_retry
        }

        return context.getString(stringResId, successCount, failedCount)
    }

    private fun getEditEtalaseRetryMessage(context: Context, result: EditByMenu): String {
        val successCount = result.success.count()
        val failedCount = result.failed.count()
        val stringResId = R.string.product_manage_edit_products_etalase_retry

        return context.getString(stringResId, successCount, failedCount)
    }

    fun getSuccessMessage(context: Context, result: MultiEditResult): String {
        return when(result) {
            is EditByStatus -> getEditByStatusSuccessMessage(context, result)
            is EditByMenu -> getEditEtalaseSuccessMessage(context, result)
        }
    }

    fun getRetryMessage(context: Context, result: MultiEditResult): String {
        return when(result) {
            is EditByStatus -> getEditByStatusRetryMessage(context, result)
            is EditByMenu -> getEditEtalaseRetryMessage(context, result)
        }
    }
}