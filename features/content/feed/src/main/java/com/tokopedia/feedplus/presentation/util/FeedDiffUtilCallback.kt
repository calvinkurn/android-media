package com.tokopedia.feedplus.presentation.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardLivePreviewContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel

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

        return if (oldItem is FeedCardImageContentModel && newItem is FeedCardImageContentModel) {
            oldItem.id == newItem.id
        } else if (oldItem is FeedCardVideoContentModel && newItem is FeedCardVideoContentModel) {
            oldItem.id == newItem.id
        } else if (oldItem is FeedCardLivePreviewContentModel && newItem is FeedCardLivePreviewContentModel) {
            oldItem.id == newItem.id
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldItemPosition >= oldListSize) return false
        if (newItemPosition >= newListSize) return false

        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }
}
