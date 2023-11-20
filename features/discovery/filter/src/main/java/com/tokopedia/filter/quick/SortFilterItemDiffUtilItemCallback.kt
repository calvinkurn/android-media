package com.tokopedia.filter.quick

import androidx.recyclerview.widget.DiffUtil.ItemCallback

internal class SortFilterItemDiffUtilItemCallback: ItemCallback<SortFilterItem>() {

    override fun areItemsTheSame(oldItem: SortFilterItem, newItem: SortFilterItem): Boolean =
        oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: SortFilterItem, newItem: SortFilterItem): Boolean =
        oldItem == newItem
}
