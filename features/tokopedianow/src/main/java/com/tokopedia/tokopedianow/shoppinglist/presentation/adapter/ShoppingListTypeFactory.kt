package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter

import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.HorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.TopAllAddToCartUiModel

interface ShoppingListTypeFactory {
    fun type(uiModel: HorizontalProductCardItemUiModel): Int
    fun type(uiModel: TopAllAddToCartUiModel): Int
}
