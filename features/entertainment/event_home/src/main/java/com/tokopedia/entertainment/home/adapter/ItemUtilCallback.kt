package com.tokopedia.entertainment.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemModel

/**
 * Author errysuprayogi on 24,February,2020
 */
class ItemUtilCallback(var oldItem: List<EventItemModel>, var newItem: List<EventItemModel>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItem.size
    }

    override fun getNewListSize(): Int {
        return newItem.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItem[oldItemPosition].isLiked == newItem[newItemPosition].isLiked
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItem[oldItemPosition] == newItem[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}