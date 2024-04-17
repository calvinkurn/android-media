package com.tokopedia.tokopedianow.shoppinglist.domain.extension

import com.tokopedia.tokopedianow.shoppinglist.domain.model.SaveShoppingListStateActionParam
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.Constant.INVALID_INDEX

object CheckUncheckParamExtension {
    fun MutableList<SaveShoppingListStateActionParam>.update(
        productId: String,
        isSelected: Boolean
    ) {
        val index = indexOfFirst { it.productId == productId }
        if (index != INVALID_INDEX) {
            this[index] = SaveShoppingListStateActionParam(productId, isSelected)
        } else {
            add(SaveShoppingListStateActionParam(productId, isSelected))
        }
    }

    fun MutableList<SaveShoppingListStateActionParam>.selectAll(
        products: List<ShoppingListHorizontalProductCardItemUiModel>
    ) {
        clear()
        addAll(products.map { SaveShoppingListStateActionParam(productId = it.id, isSelected = it.isSelected) })
    }
}
