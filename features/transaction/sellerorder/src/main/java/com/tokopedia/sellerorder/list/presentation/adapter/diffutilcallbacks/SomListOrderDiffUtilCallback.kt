package com.tokopedia.sellerorder.list.presentation.adapter.diffutilcallbacks

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
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

    private fun isTheSameOrder(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is SomListOrderUiModel && newItem is SomListOrderUiModel &&
                oldItem.orderId == newItem.orderId
    }

    private fun isTheSameEmptyState(oldItem: Visitable<*>?, newItem: Visitable<*>?): Boolean {
        return oldItem is SomListEmptyStateUiModel && newItem is SomListEmptyStateUiModel
    }
}