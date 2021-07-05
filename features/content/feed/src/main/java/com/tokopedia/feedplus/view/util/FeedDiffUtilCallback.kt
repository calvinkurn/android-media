package com.tokopedia.feedplus.view.util

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.poll.PollContentViewModel

internal class FeedDiffUtilCallback(
        private val oldList: List<Visitable<*>> = listOf(),
        private val newList: List<Visitable<*>> = listOf()
): DiffUtil.Callback() {

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        if (oldPosition >= oldList.size) return false
        if (newPosition >= newList.size) return false

        val oldItem = oldList[oldPosition]
        val newItem = newList[newPosition]

        return if (oldItem is DynamicPostViewModel && newItem is DynamicPostViewModel) {
            areFeedItemsTheSame(oldItem, newItem)
        } else oldItem::class == newItem::class
    }

    private fun areFeedItemsTheSame(oldItem: DynamicPostViewModel, newItem: DynamicPostViewModel): Boolean {
        return oldItem.id == newItem.id
                && oldItem.footer.like.value == newItem.footer.like.value
                && oldItem.footer.comment.value == newItem.footer.comment.value
                && !isVoteItemChanged(oldItem, newItem)
    }

    private fun isVoteItemChanged(oldItem: DynamicPostViewModel, newItem: DynamicPostViewModel): Boolean {
        try {
            if (oldItem.contentList[0] is PollContentViewModel) {
                for (pos in 0..oldItem.contentList.size) {
                    if ((oldItem.contentList[pos] as PollContentViewModel).voted != (newItem.contentList[pos] as PollContentViewModel).voted) {
                        return true
                    }
                }
            }
        } catch (e: Throwable) {
            return oldItem == newItem
        }
        return false
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
        else !(oldItem is CarouselPlayCardViewModel && newItem is CarouselPlayCardViewModel)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return if (oldList[oldItemPosition] is CarouselPlayCardViewModel && newList[newItemPosition] is CarouselPlayCardViewModel) Unit
        else super.getChangePayload(oldItemPosition, newItemPosition)
    }
}