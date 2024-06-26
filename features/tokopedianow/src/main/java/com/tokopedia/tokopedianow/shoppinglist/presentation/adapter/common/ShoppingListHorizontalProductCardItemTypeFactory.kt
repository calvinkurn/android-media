package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.common

import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel

interface ShoppingListHorizontalProductCardItemTypeFactory {
    fun type(uiModel: ShoppingListHorizontalProductCardItemUiModel): Int
}
