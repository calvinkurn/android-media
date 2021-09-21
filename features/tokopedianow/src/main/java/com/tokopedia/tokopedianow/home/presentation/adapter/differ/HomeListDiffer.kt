package com.tokopedia.tokopedianow.home.presentation.adapter.differ

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowDiffer
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel

class HomeListDiffer : BaseTokopediaNowDiffer() {
    private var oldList: List<Visitable<*>> = emptyList()
    private var newList: List<Visitable<*>> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is HomeLayoutUiModel && newItem is HomeLayoutUiModel) {
            oldItem.visitableId == newItem.visitableId
        } else if (oldItem is HomeComponentVisitable && newItem is HomeComponentVisitable) {
            oldItem.visitableId() == newItem.visitableId()
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
    ): BaseTokopediaNowDiffer {
        this.oldList = oldList
        this.newList = newList
        return this
    }
}