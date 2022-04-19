package com.tokopedia.feedplus.view.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardViewModel

internal class FeedDiffUtilCallback(
        private val oldList: List<Visitable<*>> = listOf(),
        private val newList: List<Visitable<*>> = listOf()
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        if (oldPosition >= oldList.size) return false
        if (newPosition >= newList.size) return false

        val oldItem = oldList[oldPosition]
        val newItem = newList[newPosition]

        return if (oldItem is DynamicPostUiModel && newItem is DynamicPostUiModel) {
            areFeedItemsTheSame(oldItem, newItem)
        } else oldItem::class == newItem::class
    }

    private fun areFeedItemsTheSame(
        oldItem: DynamicPostUiModel,
        newItem: DynamicPostUiModel
    ): Boolean {
        return oldItem.feedXCard.id == newItem.feedXCard.id
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

        return if (oldItem is DynamicPostUiModel && newItem is DynamicPostUiModel) oldItem == newItem
        else !(oldItem is CarouselPlayCardViewModel && newItem is CarouselPlayCardViewModel)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return if (oldList[oldItemPosition] is CarouselPlayCardViewModel && newList[newItemPosition] is CarouselPlayCardViewModel) Unit
        else super.getChangePayload(oldItemPosition, newItemPosition)
    }
}