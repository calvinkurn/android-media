package com.tokopedia.product.manage.feature.multiedit.ui.toast

import android.content.Context
import com.tokopedia.product.manage.common.feature.variant.presentation.data.EditVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus.INACTIVE

object MultiVariantToastMessage {

    private fun getEditByStatusSuccessMessage(context: Context?, result: EditVariantResult): String {
        return if (result.variantNameUpdates.size == 1){
            val variantName = result.variantNameUpdates.firstOrNull().orEmpty()
            val status = result.variants.filter {
                it.name == variantName
            }.firstOrNull()?.status

            if (status ==  INACTIVE){
                context?.getString(
                    com.tokopedia.product.manage.common.R.string.product_manage_quick_edit_status_variant_inactive_success,
                    result.productName, variantName
                ).orEmpty()
            }else{
                context?.getString(
                    com.tokopedia.product.manage.common.R.string.product_manage_quick_edit_status_variant_active_success,
                    result.productName, variantName
                ).orEmpty()
            }
        }else{
            context?.getString(
                com.tokopedia.product.manage.common.R.string.product_manage_quick_edit_stock_success,
                result.productName
            ).orEmpty()
        }
    }

    fun getSuccessMessage(context: Context?, result: EditVariantResult): String {
        return if (result.editStock) {
            context?.getString(
                com.tokopedia.product.manage.common.R.string.product_manage_quick_edit_stock_success,
                result.productName
            ).orEmpty()
        }else{
            getEditByStatusSuccessMessage(context, result)
        }
    }
}
