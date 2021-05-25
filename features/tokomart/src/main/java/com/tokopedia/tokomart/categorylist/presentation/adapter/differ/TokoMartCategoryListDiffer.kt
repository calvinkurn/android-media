package com.tokopedia.tokomart.categorylist.presentation.adapter.differ

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.common.base.adapter.BaseTokoMartDiffer
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListChildUiModel
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListItemUiModel

class TokoMartCategoryListDiffer : BaseTokoMartDiffer() {
    private var oldList: List<Visitable<*>> = emptyList()
    private var newList: List<Visitable<*>> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is CategoryListItemUiModel && newItem is CategoryListItemUiModel) {
            oldItem.id == newItem.id
        } else if (oldItem is CategoryListChildUiModel && newItem is CategoryListChildUiModel) {
            oldItem.id == newItem.id
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun create(
        oldList: List<Visitable<*>>,
        newList: List<Visitable<*>>
    ): BaseTokoMartDiffer {
        this.oldList = oldList
        this.newList = newList
        return this
    }
}