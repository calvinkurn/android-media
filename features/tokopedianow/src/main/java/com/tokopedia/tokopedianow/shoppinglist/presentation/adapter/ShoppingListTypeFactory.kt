package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter

import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListTopCheckAllUiModel

interface ShoppingListTypeFactory {
    fun type(uiModel: ShoppingListHorizontalProductCardItemUiModel): Int
    fun type(uiModel: ShoppingListTopCheckAllUiModel): Int
}
