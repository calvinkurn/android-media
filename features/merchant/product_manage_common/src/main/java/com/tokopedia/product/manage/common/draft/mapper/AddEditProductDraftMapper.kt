package com.tokopedia.product.manage.common.draft.mapper

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil
import com.tokopedia.product.manage.common.draft.data.db.entity.AddEditProductDraftEntity
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft

class AddEditProductDraftMapper {

    companion object {
        const val UTF_8 = "UTF-8"
        const val VERSION_PRODUCT_VIEW_MODEL = 1

        fun mapProductInputToJsonString(productInputModel: ProductDraft): String {
            return CacheUtil.convertModelToString(productInputModel, object : TypeToken<ProductDraft>() {}.type)
        }

        fun mapDraftToProductInput(draft : AddEditProductDraftEntity): ProductDraft {
            val productDraft: ProductDraft = CacheUtil.convertStringToModel<ProductDraft>(draft.data, ProductDraft::class.java)
            return  productDraft.copy(id = draft.id)
        }
    }
}
