package com.tokopedia.product.addedit.draft.mapper

import android.text.TextUtils
import com.tokopedia.product.manage.common.draft.data.db.model.AddEditProductDraftModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel

class AddEditProductDraftListMapper {

    companion object{
        private const val MAX_COMPLETION_COUNT = 5
        private const val MIN_COMPLETION_PERCENT = 5
        private const val MAX_COMPLETION_PERCENT = 95

        fun mapDomainToView(product: ProductInputModel, productId: Long): AddEditProductDraftModel {

            val pictures = product.detailInputModel.pictureList
            val primaryImageUrl = if(pictures.isNotEmpty()) {
                val picture = pictures[0]
                picture.urlOriginal
            } else {
                ""
            }

            var productName = product.detailInputModel.productName
            val productPrice  = product.detailInputModel.price
            val categoryId = product.detailInputModel.categoryId
            val productWeight = product.shipmentInputModel.weight

            var completionCount = 0
            var completionPercent: Int

            if (!TextUtils.isEmpty(productName)) {
                completionCount++
            }
            if (categoryId.toLong() > 0) {
                completionCount++
            }
            if (productPrice > 0) {
                completionCount++
            }
            if (productWeight > 0) {
                completionCount++
            }

            completionPercent = if (completionCount == MAX_COMPLETION_COUNT) {
                MAX_COMPLETION_PERCENT
            } else {
                completionCount * 100 / MAX_COMPLETION_COUNT
            }
            if (completionPercent == 0) {
                completionPercent = MIN_COMPLETION_PERCENT
            }

            return AddEditProductDraftModel(
                    productId,
                    primaryImageUrl,
                    productName,
                    completionPercent,
                    !TextUtils.isEmpty(productId.toString()))

        }
    }
}