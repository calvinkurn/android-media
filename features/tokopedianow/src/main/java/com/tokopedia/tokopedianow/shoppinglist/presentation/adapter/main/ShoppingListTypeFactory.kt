package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main

import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListEmptyUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListRetryUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListProductInCartUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel

interface ShoppingListTypeFactory {
    fun type(uiModel: ShoppingListProductInCartUiModel): Int
    fun type(uiModel: ShoppingListTopCheckAllUiModel): Int
    fun type(uiModel: ShoppingListRetryUiModel): Int
    fun type(uiModel: ShoppingListEmptyUiModel): Int
}
