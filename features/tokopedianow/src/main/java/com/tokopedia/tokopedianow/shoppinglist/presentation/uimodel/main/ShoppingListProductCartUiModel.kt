package com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListTypeFactory

data class ShoppingListProductCartUiModel(
    val id: String = String.EMPTY,
    val productList: List<ShoppingListProductCartItemUiModel>
) : Visitable<ShoppingListTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: ShoppingListTypeFactory): Int = typeFactory.type(this)
}
