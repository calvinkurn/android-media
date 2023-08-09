package com.tokopedia.filter.quick

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

internal class SortFilterAdapter(
    private val listener: SortFilterItemViewHolder.Listener,
): ListAdapter<SortFilterItem, SortFilterItemViewHolder>(
    SortFilterItemDiffUtilItemCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortFilterItemViewHolder =
        SortFilterItemViewHolder(
            LayoutInflater.from(parent.context).inflate(SortFilterItemViewHolder.LAYOUT, null),
            listener,
        )

    override fun onBindViewHolder(holder: SortFilterItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
