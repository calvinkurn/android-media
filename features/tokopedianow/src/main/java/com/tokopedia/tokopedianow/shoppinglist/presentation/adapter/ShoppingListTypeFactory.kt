package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter

import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListProductInCartUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.ShoppingListTopCheckAllUiModel

interface ShoppingListTypeFactory {
    fun type(uiModel: ShoppingListProductInCartUiModel): Int
    fun type(uiModel: ShoppingListTopCheckAllUiModel): Int
}
