package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter

import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.HorizontalProductCardItemUiModel

interface ShoppingListTypeFactory {
    fun type(uiModel: HorizontalProductCardItemUiModel): Int
}
