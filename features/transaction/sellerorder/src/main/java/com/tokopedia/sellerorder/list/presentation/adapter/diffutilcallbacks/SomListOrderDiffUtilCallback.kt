package com.tokopedia.sellerorder.list.presentation.adapter.diffutilcallbacks

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerorder.list.presentation.adapter.viewholders.SomListOrderEmptyViewHolder
import com.tokopedia.sellerorder.list.presentation.models.SomListEmptyStateUiModel
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel

class SomListOrderDiffUtilCallback(
        private val oldItems: List<Visitable<*>>,
        private val newItems: List<Visitable<*>>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return isTheSameOrder(oldItem, newItem) || isTheSameEmptyState(oldItem, newItem)
    }

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return oldItem == newItem
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return if (oldItem is SomListEmptyStateUiModel && newItem is SomListEmptyStateUiModel) {
            Bundle().apply {
                if (oldItem.title != newItem.title) {
                    putString(SomListOrderEmptyViewHolder.PAYLOAD_TITLE_CHANGES, newItem.title)
                }
                if (oldItem.description != newItem.description) {
                    putString(SomListOrderEmptyViewHolder.PAYLOAD_DESCRIPTION_CHANGES, newItem.description)
                }
                if (oldItem.imageUrl != newItem.imageUrl) {
                    putString(SomListOrderEmptyViewHolder.PAYLOAD_ILLUSTRATION_CHANGES, newItem.imageUrl)
                }
                if (oldItem.showButton != newItem.showButton) {
                    putBoolean(SomListOrderEmptyViewHolder.PAYLOAD_SHOW_BUTTON_CHANGES, newItem.showButton)
                }
                if (newItem.showButton) {
                    if (oldItem.buttonAppLink != newItem.buttonAppLink) {
                        putString(SomListOrderEmptyViewHolder.PAYLOAD_BUTTON_APPLINK_CHANGES, newItem.buttonAppLink)
                    }
                    if (oldItem.buttonText != newItem.buttonText) {
                        putString(SomListOrderEmptyViewHolder.PAYLOAD_BUTTON_TEXT_CHANGES, newItem.buttonText)
                    }
                }
            }
        } else if (oldItem is SomListOrderUiModel && newItem is SomListOrderUiModel) {
            oldItem to newItem
        } else super.getChangePayload(oldItemPosition, newItemPosition)
    }

    private fun isTheSameOrder(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is SomListOrderUiModel && newItem is SomListOrderUiModel &&
                oldItem.orderId == newItem.orderId
    }

    private fun isTheSameEmptyState(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is SomListEmptyStateUiModel && newItem is SomListEmptyStateUiModel
    }
}