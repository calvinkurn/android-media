package com.tokopedia.feedplus.view.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel

internal class FeedDiffUtilCallback(
        private val oldList: List<Visitable<*>> = listOf(),
        private val newList: List<Visitable<*>> = listOf()
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        if (oldPosition >= oldList.size) return false
        if (newPosition >= newList.size) return false

        val oldItem = oldList[oldPosition]
        val newItem = newList[newPosition]

        return if (oldItem is DynamicPostViewModel && newItem is DynamicPostViewModel)
            areFeedItemsTheSame(oldItem, newItem)
        else
            oldItem::class == newItem::class
    }

    private fun areFeedItemsTheSame(oldItem: DynamicPostViewModel, newItem: DynamicPostViewModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        if (oldPosition >= oldList.size) return false
        if (newPosition >= newList.size) return false

        val oldItem = oldList[oldPosition]
        val newItem = newList[newPosition]

        return if (oldItem is DynamicPostViewModel && newItem is DynamicPostViewModel) oldItem == newItem
        else true
    }
}