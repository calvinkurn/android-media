package com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListTypeFactory

data class ShoppingListLoadingMoreUiModel(
    val id: String = String.EMPTY
): Visitable<ShoppingListTypeFactory> {
    override fun type(typeFactory: ShoppingListTypeFactory): Int = typeFactory.type(this)
}
