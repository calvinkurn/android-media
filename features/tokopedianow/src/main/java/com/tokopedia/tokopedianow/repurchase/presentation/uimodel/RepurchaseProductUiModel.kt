package com.tokopedia.tokopedianow.repurchase.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.repurchase.presentation.adapter.RepurchaseTypeFactory

data class RepurchaseProductUiModel(
    val id: String,
    val parentId: String,
    val shopId: String,
    val categoryId: String,
    val category: String,
    val isStockEmpty: Boolean,
    val productCard: ProductCardModel
) : Visitable<RepurchaseTypeFactory>, ImpressHolder() {

    override fun type(typeFactory: RepurchaseTypeFactory): Int {
        return typeFactory.type(this)
    }
}
