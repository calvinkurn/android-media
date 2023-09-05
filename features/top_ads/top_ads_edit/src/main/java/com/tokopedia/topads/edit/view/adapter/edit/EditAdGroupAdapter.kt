package com.tokopedia.topads.edit.view.adapter.edit

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemAdsPotentialUiModel
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemAdsPotentialWidgetUiModel
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemState
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemTag
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemUiModel
import com.tokopedia.topads.edit.view.viewholder.EditAdGroupItemViewHolder

class EditAdGroupAdapter(
    typeFactory: EditAdGroupTypeFactory?
) : BaseListAdapter<Visitable<*>, EditAdGroupTypeFactory>(typeFactory) {

    fun updateList(newList: List<Visitable<*>>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun updatePotentialWidget(potentialWidgetList: MutableList<EditAdGroupItemAdsPotentialWidgetUiModel>) {
        val indexOfUpdate = list.indexOfFirst { it is EditAdGroupItemAdsPotentialUiModel }
        val itemToUpdate = (list[indexOfUpdate] as EditAdGroupItemAdsPotentialUiModel)
        itemToUpdate?.let {
            it.listWidget = potentialWidgetList
            it.state = EditAdGroupItemState.LOADED
        }
        notifyItemChanged(indexOfUpdate)
    }

    fun updateValue(desiredTag: EditAdGroupItemTag, value: String) {
        val itemToUpdate =
            list.find { it is EditAdGroupItemUiModel && it.tag == desiredTag } as EditAdGroupItemUiModel
        val indexOfUpdate = list.indexOf(itemToUpdate)
        itemToUpdate.subtitle = value
        notifyItemChanged(indexOfUpdate)
    }

    fun removeItem(desiredTag: EditAdGroupItemTag) {
        val itemToRemove =
            list.find { it is EditAdGroupItemUiModel && it.tag == desiredTag } as EditAdGroupItemUiModel
        val positionToRemove = list.indexOf(itemToRemove)
        if (positionToRemove >= 0) {
            list.removeAt(positionToRemove)
            notifyItemRemoved(positionToRemove)
        }

    }


}