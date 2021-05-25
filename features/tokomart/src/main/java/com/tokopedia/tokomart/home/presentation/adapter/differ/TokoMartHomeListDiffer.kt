package com.tokopedia.tokomart.home.presentation.adapter.differ

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokomart.common.base.adapter.BaseTokoMartDiffer
import com.tokopedia.tokomart.home.presentation.uimodel.TokoMartHomeLayoutUiModel

class TokoMartHomeListDiffer : BaseTokoMartDiffer() {
    private var oldList: List<Visitable<*>> = emptyList()
    private var newList: List<Visitable<*>> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is TokoMartHomeLayoutUiModel && newItem is TokoMartHomeLayoutUiModel) {
            oldItem.channelId == newItem.channelId
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
    ): BaseTokoMartDiffer {
        this.oldList = oldList
        this.newList = newList
        return this
    }
}