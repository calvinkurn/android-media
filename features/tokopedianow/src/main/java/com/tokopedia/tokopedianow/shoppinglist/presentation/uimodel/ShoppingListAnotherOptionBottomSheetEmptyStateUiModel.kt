package com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.anotheroptionbottomsheet.ShoppingListAnotherOptionBottomSheetEmptyStateTypeFactory

data class ShoppingListAnotherOptionBottomSheetEmptyStateUiModel(
    val id: String = String.EMPTY,
) : Visitable<ShoppingListAnotherOptionBottomSheetEmptyStateTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: ShoppingListAnotherOptionBottomSheetEmptyStateTypeFactory): Int = typeFactory.type(this)
}
