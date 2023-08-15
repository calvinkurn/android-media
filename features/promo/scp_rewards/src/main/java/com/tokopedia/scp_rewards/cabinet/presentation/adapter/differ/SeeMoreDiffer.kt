package com.tokopedia.scp_rewards.cabinet.presentation.adapter.differ

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.scp_rewards_widgets.medal.MedalItem

class SeeMoreDiffer(private val newList:List<Visitable<*>>, private val oldList:List<Visitable<*>>) : DiffUtil.Callback(){

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        if(oldItem::class.simpleName != newItem::class.simpleName) return false
        if(oldItem is MedalItem && newItem is MedalItem){
            return oldItem.id == newItem.id
        }
        return true
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        if(oldItem is MedalItem && newItem is MedalItem) return oldItem == newItem
        return true
    }

    override fun getNewListSize() = newList.size

    override fun getOldListSize() = oldList.size
}
