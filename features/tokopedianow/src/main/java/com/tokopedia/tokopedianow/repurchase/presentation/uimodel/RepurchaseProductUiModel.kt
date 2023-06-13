package com.tokopedia.tokopedianow.repurchase.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.adapter.RepurchaseTypeFactory

data class RepurchaseProductUiModel(
    val parentId: String,
    val shopId: String,
    val categoryId: String,
    val category: String,
    val position: Int,
    val productCardModel: ProductCardCompactUiModel,
) : Visitable<RepurchaseTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: RepurchaseTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getChangePayload(repurchaseProduct: RepurchaseProductUiModel): Any? {
        val newProductCard = repurchaseProduct.productCardModel
        return when {
            productCardModel != newProductCard -> true
            else -> null
        }
    }
}
