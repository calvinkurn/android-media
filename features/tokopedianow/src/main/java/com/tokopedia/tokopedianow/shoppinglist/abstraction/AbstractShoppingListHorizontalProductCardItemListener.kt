package com.tokopedia.tokopedianow.shoppinglist.abstraction

import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewholder.common.ShoppingListHorizontalProductCardItemViewHolder

abstract class AbstractShoppingListHorizontalProductCardItemListener: ShoppingListHorizontalProductCardItemViewHolder.ShoppingListHorizontalProductCardItemListener {
    override fun onSelectCheckbox(product: ShoppingListHorizontalProductCardItemUiModel, isSelected: Boolean) { /* do nothing */ }

    override fun onClickAnotherOption(product: ShoppingListHorizontalProductCardItemUiModel) { /* do nothing */ }

    override fun onClickDeleteIcon(product: ShoppingListHorizontalProductCardItemUiModel) { /* do nothing */ }
}
