package com.tokopedia.topads.view.adapter.adgrouplist

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topads.view.adapter.adgrouplist.model.AdGroupUiModel
import com.tokopedia.topads.view.adapter.adgrouplist.model.ErrorUiModel

class AdGroupListDiffer(
    private val oldList:List<Visitable<*>>,
    private val newList:List<Visitable<*>>
    ) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return if(oldItem is AdGroupUiModel && newItem is AdGroupUiModel) oldItem.groupId == newItem.groupId
        else if(oldItem is ErrorUiModel && newItem is ErrorUiModel) oldItem.errorType == newItem.errorType
        else oldItem::class == newItem::class
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.toString() == newItem.toString()
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        if(oldItem is AdGroupUiModel && newItem is AdGroupUiModel){
            if(oldItem.selected!=newItem.selected) return newItem.selected
            else if(oldItem.adGroupStats!=newItem.adGroupStats) return newItem.adGroupStats
        }
        else if(oldItem is ErrorUiModel && newItem is ErrorUiModel) return newItem.errorType
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}
