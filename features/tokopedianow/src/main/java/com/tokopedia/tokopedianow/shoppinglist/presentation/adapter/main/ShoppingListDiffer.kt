package com.tokopedia.tokopedianow.shoppinglist.presentation.adapter.main

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowDiffer
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.main.ShoppingListCartProductUiModel

class ShoppingListDiffer : BaseTokopediaNowDiffer() {
    private var oldList: List<Visitable<*>> = emptyList()
    private var newList: List<Visitable<*>> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if (oldItem is ShoppingListHorizontalProductCardItemUiModel && newItem is ShoppingListHorizontalProductCardItemUiModel) {
            oldItem.id == newItem.id
        } else {
            oldItem == newItem
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is ShoppingListHorizontalProductCardItemUiModel && newItem is ShoppingListHorizontalProductCardItemUiModel) {
            oldItem.getChangePayload(newItem)
        } else {
            super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is ShoppingListHorizontalProductCardItemUiModel && newItem is ShoppingListHorizontalProductCardItemUiModel) {
            oldItem.isSelected == newItem.isSelected &&
            oldItem.productLayoutType == newItem.productLayoutType &&
            oldItem.state == newItem.state
        } else if (oldItem is ShoppingListCartProductUiModel && newItem is ShoppingListCartProductUiModel) {
            oldItem.productList == newItem.productList
        } else {
            oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun create(
        oldList: List<Visitable<*>>,
        newList: List<Visitable<*>>
    ): BaseTokopediaNowDiffer {
        this.oldList = oldList
        this.newList = newList
        return this
    }
}
