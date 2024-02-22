package com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main.ShoppingListTypeFactory
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel

data class ShoppingListExpandCollapseUiModel(
    val id: String = String.EMPTY,
    val remainingTotalProduct: Int,
    val state: State,
    val productLayoutType: ShoppingListHorizontalProductCardItemUiModel.Type
) : Visitable<ShoppingListTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: ShoppingListTypeFactory): Int = typeFactory.type(this)

    enum class State {
        EXPAND,
        COLLAPSE
    }
}
