package com.tokopedia.product.manage.common.feature.draft.mapper

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.product.manage.common.feature.draft.data.db.entity.AddEditProductDraftEntity
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft

class AddEditProductDraftMapper {

    companion object {
        private const val MIN_COMPLETION_PERCENT = 5
        private const val MAX_COMPLETION_PERCENT = 95
        private const val PLACE_HOLDER = "PLACE_HOLDER"

        fun mapProductInputToJsonString(product: ProductDraft): String {
            return CacheUtil.convertModelToString(product, object : TypeToken<ProductDraft>() {}.type)
        }

        fun mapDraftToProductInput(draft: AddEditProductDraftEntity): ProductDraft {
            val productDraft: ProductDraft = CacheUtil.convertStringToModel(draft.data, ProductDraft::class.java)
            productDraft.draftId = draft.id
            return productDraft
        }

        fun getCompletionPercent(product: ProductDraft): Int {
            var completionCount = 0
            var completionPercent: Int

            val productPicture = product.detailInputModel.imageUrlOrPathList
            if (!productPicture.isNullOrEmpty() && productPicture.firstOrNull() != PLACE_HOLDER) {
                completionCount++
            }

            val productName = product.detailInputModel.productName
            if (productName.isNotBlank()) {
                completionCount++
            }

            val categoryId = product.detailInputModel.categoryId
            if (categoryId.isNotBlank()) {
                completionCount++
            }

            val price = product.detailInputModel.price
            if (price > 0.toBigInteger()) {
                completionCount++
            }

            val stock = product.detailInputModel.stock
            if (stock > 0) {
                completionCount++
            }

            val condition = product.detailInputModel.condition
            if (condition.isNotEmpty()) {
                completionCount++
            }

            val weight = product.shipmentInputModel.weight
            if (weight > 0) {
                completionCount++
            }

            completionPercent = (100 / 7) * completionCount
            if (completionPercent == 0) {
                completionPercent = MIN_COMPLETION_PERCENT
            } else if(completionPercent > MAX_COMPLETION_PERCENT)
                completionPercent = MAX_COMPLETION_PERCENT

            return completionPercent
        }
    }
}
