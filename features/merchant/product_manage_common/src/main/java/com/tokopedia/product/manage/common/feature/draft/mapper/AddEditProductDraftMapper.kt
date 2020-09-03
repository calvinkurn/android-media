package com.tokopedia.product.manage.common.feature.draft.mapper

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.product.manage.common.feature.draft.data.db.entity.AddEditProductDraftEntity
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft

class AddEditProductDraftMapper {

    companion object {
        private const val MAX_COMPLETION_COUNT = 5
        private const val MIN_COMPLETION_PERCENT = 5
        private const val MAX_COMPLETION_PERCENT = 95

        fun mapProductInputToJsonString(product: ProductDraft): String {
            return CacheUtil.convertModelToString(product, object : TypeToken<ProductDraft>() {}.type)
        }

        fun mapDraftToProductInput(draft: AddEditProductDraftEntity): ProductDraft {
            val productDraft: ProductDraft = CacheUtil.convertStringToModel(draft.data, ProductDraft::class.java)
            productDraft.draftId = draft.id
            productDraft.completionPercent = getCompletionPercent(productDraft)
            return productDraft
        }

        private fun getCompletionPercent(product: ProductDraft): Int {

            val productName = product.detailInputModel.productName
            val categoryId = product.detailInputModel.categoryId
            val price = product.detailInputModel.price
            val stock = product.detailInputModel.stock
            val minOrder = product.detailInputModel.minOrder
            val condition = product.detailInputModel.condition
            val weight = product.shipmentInputModel.weight

            var completionCount = 0
            var completionPercent: Int

            if (productName.isNotEmpty()) {
                completionCount++
            }
            if (categoryId.isNotEmpty()) {
                completionCount++
            }
            if (price > 0.toBigInteger()) {
                completionCount++
            }
            if (stock > 0) {
                completionCount++
            }
            if (minOrder > 0) {
                completionCount++
            }
            if (condition.isNotEmpty()) {
                completionCount++
            }
            if (weight > 0) {
                completionCount++
            }

            completionPercent = (100 / 7) * completionCount

            if (completionPercent == 0) {
                completionPercent = MIN_COMPLETION_PERCENT
            } else if(completionPercent > MAX_COMPLETION_PERCENT)
                completionPercent = MAX_COMPLETION_COUNT

            return completionPercent
        }
    }
}
