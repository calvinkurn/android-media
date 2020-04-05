package com.tokopedia.product.addedit.draft.mapper

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.product.addedit.draft.data.db.entity.AddEditProductDraftEntity
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.addedit.preview.domain.mapper.GetProductMapper
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import rx.functions.Func1

class AddEditProductDraftMapper {

    companion object {
        const val UTF_8 = "UTF-8"
        const val VERSION_PRODUCT_VIEW_MODEL = 1

        fun mapProductInputToJsonString(productInputModel: ProductInputModel): String {
            return CacheUtil.convertModelToString(productInputModel, object : TypeToken<Product>() {}.type)
        }

        fun mapDraftToProductInput(draft : AddEditProductDraftEntity): ProductInputModel {
            val productInputModel: ProductInputModel
            productInputModel = try {
                CacheUtil.convertStringToModel(draft.data, ProductInputModel::class.java)
            }catch (e: Exception) {
                val product: Product = CacheUtil.convertStringToModel(draft.data, Product::class.java)
                GetProductMapper().mapRemoteModelToUiModel(product)
            }
            return productInputModel
        }
    }




}
