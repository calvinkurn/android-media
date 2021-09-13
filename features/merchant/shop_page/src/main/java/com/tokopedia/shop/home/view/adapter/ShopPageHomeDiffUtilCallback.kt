package com.tokopedia.shop.home.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

class ShopPageHomeDiffUtilCallback (
        private val oldItems: List<Visitable<*>>,
        private val newItems: List<Visitable<*>>,
        private var indexToRefresh: Int
): DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if (isRefreshItemAtPosition(newItemPosition))
            false
        else {
            val oldItem = oldItems.getOrNull(oldItemPosition)
            val newItem = newItems.getOrNull(newItemPosition)
            return oldItem == newItem
        }
    }

    private fun isRefreshItemAtPosition(newItemPosition: Int): Boolean {
        return newItemPosition == indexToRefresh && indexToRefresh >= 0
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems.getOrNull(oldItemPosition) == newItems.getOrNull(newItemPosition)
    }
}