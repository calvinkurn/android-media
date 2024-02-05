package com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.ShoppingListTypeFactory

data class ShoppingListProductInCartUiModel(
    val id: String = String.EMPTY,
    val productList: List<ShoppingListProductInCartItemUiModel>
) : Visitable<ShoppingListTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: ShoppingListTypeFactory): Int = typeFactory.type(this)
}
