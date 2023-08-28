package com.tokopedia.topads.edit.view.adapter.edit

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemAdsPotentialUiModel
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemAdsPotentialWidgetUiModel
import com.tokopedia.topads.edit.view.model.edit.EditAdGroupItemState
import com.tokopedia.topads.edit.view.viewholder.EditAdGroupItemViewHolder

class EditAdGroupAdapter (typeFactory: EditAdGroupTypeFactory?
) : BaseListAdapter<Visitable<*>, EditAdGroupTypeFactory>(typeFactory) {

    fun updateList(newList: List<Visitable<*>>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun updatePotentialWidget(potentialWidgetList: MutableList<EditAdGroupItemAdsPotentialWidgetUiModel>) {
        val indexOfUpdate = list.indexOfFirst { it is EditAdGroupItemAdsPotentialUiModel }
        val itemToUpdate = (list[indexOfUpdate] as EditAdGroupItemAdsPotentialUiModel)
        itemToUpdate?.let{
            it.listWidget = potentialWidgetList
            it.state = EditAdGroupItemState.LOADED
        }
        notifyItemChanged(indexOfUpdate)
    }

}