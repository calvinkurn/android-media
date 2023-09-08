package com.tokopedia.topads.common.view.adapter.createedit

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemAdsPotentialWidgetUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemState
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemTag
import com.tokopedia.topads.common.domain.model.createedit.CreateEditAdGroupItemUiModel
import com.tokopedia.topads.common.domain.model.createedit.CreateEditItemUiModel

class CreateEditAdGroupAdapter(
    typeFactory: CreateEditAdGroupTypeFactory?
) : BaseListAdapter<Visitable<*>, CreateEditAdGroupTypeFactory>(typeFactory) {

    fun updateList(newList: List<Visitable<*>>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun updatePotentialWidget(potentialWidgetList: MutableList<CreateEditAdGroupItemAdsPotentialWidgetUiModel>) {
        val indexOfUpdate = list.indexOfFirst { it is CreateEditAdGroupItemAdsPotentialUiModel }
        val itemToUpdate = (list[indexOfUpdate] as CreateEditAdGroupItemAdsPotentialUiModel)
        itemToUpdate.let {
            it.listWidget = potentialWidgetList
            it.state = CreateEditAdGroupItemState.LOADED
        }
        notifyItemChanged(indexOfUpdate)
    }

    fun updateValue(desiredTag: CreateEditAdGroupItemTag, value: String) {
        val itemToUpdate =
            list.find { it is CreateEditItemUiModel && it.itemTag() == desiredTag } as CreateEditItemUiModel

        val indexOfUpdate = list.indexOf(itemToUpdate)
        itemToUpdate.setItemSubtitle(value)
        notifyItemChanged(indexOfUpdate)
    }

    fun removeItem(desiredTag: CreateEditAdGroupItemTag) {
        val itemToRemove =
            list.find { it is CreateEditAdGroupItemUiModel && it.tag == desiredTag } as CreateEditAdGroupItemUiModel
        val positionToRemove = list.indexOf(itemToRemove)
        if (positionToRemove >= 0) {
            list.removeAt(positionToRemove)
            notifyItemRemoved(positionToRemove)
        }
    }
}
