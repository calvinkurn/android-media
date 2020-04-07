package com.tokopedia.product.addedit.draft.mapper

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.product.addedit.draft.data.db.entity.AddEditProductDraftEntity
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel

class AddEditProductDraftMapper {

    companion object {
        private const val MAX_COMPLETION_COUNT = 5
        private const val MIN_COMPLETION_PERCENT = 5
        private const val MAX_COMPLETION_PERCENT = 95

        fun mapProductInputToJsonString(productInputModel: ProductInputModel): String {
            return CacheUtil.convertModelToString(productInputModel, object : TypeToken<ProductInputModel>() {}.type)
        }

        fun mapDraftToProductInput(draft : AddEditProductDraftEntity): ProductInputModel {
            val productInputModel: ProductInputModel = CacheUtil.convertStringToModel(draft.data, ProductInputModel::class.java)
            Log.d("Hello Success", productInputModel.toString())
            productInputModel.productId = draft.id
            productInputModel.completionPercent = getCompletionPercent(productInputModel)
            return productInputModel
        }

        private fun getCompletionPercent(product: ProductInputModel): Int {

            val productName = product.detailInputModel.productName
            val categoryId = product.detailInputModel.categoryId
            val price  = product.detailInputModel.price
            val stock = product.detailInputModel.stock
            val minOrder = product.detailInputModel.minOrder
            val condition = product.detailInputModel.condition
            val weight = product.shipmentInputModel.weight

            var completionCount = 0
            var completionPercent: Int

            if (productName.isNotEmpty()) {
                completionCount++
            }
            if (categoryId.toLong() > 0) {
                completionCount++
            }
            if (price > 0) {
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

            completionPercent = if (completionCount == MAX_COMPLETION_COUNT) {
                MAX_COMPLETION_PERCENT
            } else {
                completionCount * 100 / MAX_COMPLETION_COUNT
            }
            if (completionPercent == 0) {
                completionPercent = MIN_COMPLETION_PERCENT
            }

            return completionPercent
        }
    }
}
