package com.tokopedia.tokopedianow.shoppinglist.helper

import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.common.ShoppingListHorizontalProductCardItemViewHolder

abstract class AbstractShoppingListHorizontalProductCardItemListener: ShoppingListHorizontalProductCardItemViewHolder.ShoppingListHorizontalProductCardItemListener {
    override fun onSelectCheckbox(productId: String, isSelected: Boolean) { /* do nothing */ }

    override fun onClickOtherOptions(productId: String) { /* do nothing */ }

    override fun onClickDeleteIcon(product: ShoppingListHorizontalProductCardItemUiModel) { /* do nothing */ }

    abstract override fun onClickAddToShoppingList(product: ShoppingListHorizontalProductCardItemUiModel)
}
