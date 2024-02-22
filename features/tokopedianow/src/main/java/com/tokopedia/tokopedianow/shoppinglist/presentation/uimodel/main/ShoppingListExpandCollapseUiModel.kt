package com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductState
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListTypeFactory

data class ShoppingListExpandCollapseUiModel(
    val id: String = String.EMPTY,
    val remainingTotalProduct: Int,
    val productState: ShoppingListProductState,
    val productLayoutType: ShoppingListProductLayoutType
) : Visitable<ShoppingListTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: ShoppingListTypeFactory): Int = typeFactory.type(this)
}
