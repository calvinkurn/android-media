package com.tokopedia.entertainment.pdp.adapter.diffutil

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.entertainment.pdp.uimodel.ParticipantTitleUiModel
import com.tokopedia.entertainment.pdp.uimodel.ParticipantUiModel

/**
 * Author firmanda on 17,Nov,2022
 */

class EventRedeemRevampDiffer : BaseEventRedeemRevampDiffer() {
    private var oldList: List<Visitable<*>> = emptyList()
    private var newList: List<Visitable<*>> = emptyList()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is ParticipantUiModel && newItem is ParticipantUiModel) {
            oldItem.id == newItem.id
        } else if (oldItem is ParticipantTitleUiModel && newItem is ParticipantTitleUiModel) {
            oldItem.id == newItem.id
        } else oldItem == newItem

    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getNewListSize(): Int = newList.size

    override fun getOldListSize(): Int = oldList.size

    override fun create(
        oldList: List<Visitable<*>>,
        newList: List<Visitable<*>>
    ): BaseEventRedeemRevampDiffer {
        this.oldList = oldList
        this.newList = newList
        return this
    }
}
