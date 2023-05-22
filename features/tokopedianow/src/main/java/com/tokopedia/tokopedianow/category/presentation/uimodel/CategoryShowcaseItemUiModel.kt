package com.tokopedia.tokopedianow.category.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryShowcaseTypeFactory

data class CategoryShowcaseItemUiModel(
    val productCardModel: ProductCardCompactUiModel = ProductCardCompactUiModel(),
    val parentProductId: String
): Visitable<CategoryShowcaseTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: CategoryShowcaseTypeFactory): Int  = typeFactory.type(this)
}
