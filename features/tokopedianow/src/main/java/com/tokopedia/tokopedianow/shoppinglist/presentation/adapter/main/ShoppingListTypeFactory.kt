package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main

import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListEmptyUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListExpandCollapseUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListRetryUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListCartProductUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListTopCheckAllUiModel

interface ShoppingListTypeFactory {
    fun type(uiModel: ShoppingListCartProductUiModel): Int
    fun type(uiModel: ShoppingListTopCheckAllUiModel): Int
    fun type(uiModel: ShoppingListRetryUiModel): Int
    fun type(uiModel: ShoppingListEmptyUiModel): Int
    fun type(uiModel: ShoppingListExpandCollapseUiModel): Int
}
