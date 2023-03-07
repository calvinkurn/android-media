package com.tokopedia.feedplus.presentation.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created By : Muhammad Furqan on 07/03/23
 */
class FeedDiffUtilCallback(
    private val oldList: List<Visitable<*>> = listOf(),
    private val newList: List<Visitable<*>> = listOf()
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition >= oldListSize) return false
        if (newItemPosition >= newListSize) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem::class == newItem::class
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition >= oldListSize) return false
        if (newItemPosition >= newListSize) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }
}
