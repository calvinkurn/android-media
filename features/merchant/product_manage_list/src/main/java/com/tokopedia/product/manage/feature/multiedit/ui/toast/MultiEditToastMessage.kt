package com.tokopedia.product.manage.feature.multiedit.ui.toast

import android.content.Context
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByMenu
import com.tokopedia.product.manage.feature.list.view.model.MultiEditResult.EditByStatus
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus.DELETED
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus.INACTIVE

object MultiEditToastMessage {

    private const val REASON_CAMPAIGN = "Locked field: product status, Usecase: campaign"
    private fun getEditByStatusSuccessMessage(
        context: Context,
        result: EditByStatus,
        itemsChecked: MutableList<ProductUiModel>
    ): String {
        val successCount = result.success.count()

        when (result.status) {
            INACTIVE -> {
                return if (successCount == 1) {
                    context.getString(
                        com.tokopedia.product.manage.common.R.string.product_manage_quick_edit_status_inactive_success,
                        itemsChecked.firstOrNull()?.title.orEmpty()
                    )
                } else {
                    context.getString(R.string.product_manage_edit_products_inactive_success)
                }
            }

            DELETED -> {
                return context.getString(R.string.product_manage_delete_products_success, successCount)
            }
            else -> {
                return context.getString(R.string.product_manage_edit_products_success, successCount)
            }
        }
    }

    private fun getEditEtalaseSuccessMessage(context: Context, result: MultiEditResult): String {
        val successCount = result.success.count()
        val stringResId = R.string.product_manage_edit_products_etalase_success

        return context.getString(stringResId, successCount)
    }

    private fun getEditByStatusRetryMessage(context: Context, result: EditByStatus): String {
        val successCount = result.success.count()
        val failedCount = result.failed.count()

        when (result.status) {
            INACTIVE -> {
                val totalFailedCampaign = result.failed.filter {
                    it.result?.header?.reason == REASON_CAMPAIGN
                }.count()

                return if (totalFailedCampaign == failedCount) {
                    val stringResId = R.string.product_manage_multi_edit_failed_toaster_campaign_deactivate
                    context.getString(stringResId, totalFailedCampaign)
                } else {
                    val stringResId = R.string.product_manage_edit_products_inactive_retry
                    context.getString(stringResId, successCount, failedCount)
                }
            }

            DELETED -> {
                val totalFailedCampaign = result.failed.filter {
                    it.result?.header?.reason == REASON_CAMPAIGN
                }.count()

                return if (totalFailedCampaign == failedCount) {
                    val stringResId = R.string.product_manage_delete_products_failed
                    context.getString(stringResId, totalFailedCampaign)
                } else {
                    val stringResId = R.string.product_manage_delete_products_retry
                    context.getString(stringResId, successCount, failedCount)
                }
            }

            else -> {
                val stringResId = R.string.product_manage_edit_products_retry
                return context.getString(stringResId, successCount, failedCount)
            }
        }
    }

    private fun getEditEtalaseRetryMessage(context: Context, result: EditByMenu): String {
        val successCount = result.success.count()
        val failedCount = result.failed.count()
        val stringResId = R.string.product_manage_edit_products_etalase_retry

        return context.getString(stringResId, successCount, failedCount)
    }

    fun getSuccessMessage(
        context: Context,
        result: MultiEditResult,
        itemsChecked: MutableList<ProductUiModel>
    ): String {
        return when (result) {
            is EditByStatus -> getEditByStatusSuccessMessage(context, result, itemsChecked)
            is EditByMenu -> getEditEtalaseSuccessMessage(context, result)
        }
    }

    fun getRetryMessage(context: Context, result: MultiEditResult): String {
        return when (result) {
            is EditByStatus -> getEditByStatusRetryMessage(context, result)
            is EditByMenu -> getEditEtalaseRetryMessage(context, result)
        }
    }
}
