package com.tokopedia.product.addedit.draft.mapper

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.product.addedit.draft.data.db.entity.AddEditProductDraftEntity
import com.tokopedia.product.addedit.preview.domain.mapper.GetProductMapper
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import rx.functions.Func1

class AddEditProductDraftMapper {

    companion object {
        const val UTF_8 = "UTF-8"
        const val VERSION_PRODUCT_VIEW_MODEL = 1

        fun mapProductInputToJsonString(productInputModel: ProductInputModel): String {
            return CacheUtil.convertModelToString(productInputModel, object : TypeToken<ProductInputModel>() {}.type)
        }

        fun mapDraftToProductInput(draft : AddEditProductDraftEntity): ProductInputModel {
            val productInputModel: ProductInputModel = CacheUtil.convertStringToModel(draft.data, ProductInputModel::class.java)
            Log.d("Hello Success", productInputModel.toString())
            productInputModel.productId = draft.id
            return productInputModel
        }
    }




}
