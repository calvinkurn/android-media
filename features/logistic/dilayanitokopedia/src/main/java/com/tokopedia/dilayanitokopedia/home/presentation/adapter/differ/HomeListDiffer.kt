package com.tokopedia.dilayanitokopedia.home.presentation.adapter.differ

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.dilayanitokopedia.common.base.adapter.BaseDtDiffer

class HomeListDiffer : BaseDtDiffer() {
    private var oldList: List<Visitable<*>> = emptyList()
    private var newList: List<Visitable<*>> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList.getOrNull(oldItemPosition)
        val newItem = newList.getOrNull(newItemPosition)
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun create(
        oldList: List<Visitable<*>>,
        newList: List<Visitable<*>>
    ): BaseDtDiffer {
        this.oldList = oldList
        this.newList = newList
        return this
    }
}
