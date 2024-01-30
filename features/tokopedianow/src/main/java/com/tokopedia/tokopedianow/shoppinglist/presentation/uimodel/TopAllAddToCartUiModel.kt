package com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.ShoppingListTypeFactory

data class TopAllAddToCartUiModel(
    val id: String,
    val allPrice: String,
    val selectedProductCounter: String
): Visitable<ShoppingListTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: ShoppingListTypeFactory): Int = typeFactory.type(this)
}
