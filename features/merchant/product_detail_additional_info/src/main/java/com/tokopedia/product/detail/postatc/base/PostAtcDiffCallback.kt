package com.tokopedia.product.detail.postatc.base

import androidx.recyclerview.widget.DiffUtil

class PostAtcDiffCallback(
    private val oldItems: List<PostAtcUiModel>,
    private val newItems: List<PostAtcUiModel>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem.equalsWith(newItem)
    }
}
