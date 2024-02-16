package com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.bottomsheet

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.bottomsheet.ShoppingListAnotherOptionBottomSheetErrorStateTypeFactory

data class ShoppingListAnotherOptionBottomSheetErrorStateUiModel(
    val id: String = String.EMPTY,
    val throwable: Throwable
) : Visitable<ShoppingListAnotherOptionBottomSheetErrorStateTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: ShoppingListAnotherOptionBottomSheetErrorStateTypeFactory): Int = typeFactory.type(this)
}
