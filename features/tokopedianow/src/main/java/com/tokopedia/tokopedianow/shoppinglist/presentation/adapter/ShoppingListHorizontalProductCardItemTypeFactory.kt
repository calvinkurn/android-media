package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter

import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListHorizontalProductCardItemUiModel

interface ShoppingListHorizontalProductCardItemTypeFactory {
    fun type(uiModel: ShoppingListHorizontalProductCardItemUiModel): Int
}
