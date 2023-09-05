package com.tokopedia.tokopedianow.category.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryShowcaseTypeFactory

data class CategoryShowcaseItemUiModel(
    val index: Int = 0,
    val productCardModel: ProductCardCompactUiModel = ProductCardCompactUiModel(),
    val parentProductId: String,
    val headerName: String,
    val shopId: String = ""
) : Visitable<CategoryShowcaseTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: CategoryShowcaseTypeFactory): Int = typeFactory.type(this)

    fun getChangePayload(categoryShowcaseItem: CategoryShowcaseItemUiModel): Any? {
        val newProductCard = categoryShowcaseItem.productCardModel
        return when {
            productCardModel != newProductCard -> true
            else -> null
        }
    }

    fun getProductId() = productCardModel.productId
}
