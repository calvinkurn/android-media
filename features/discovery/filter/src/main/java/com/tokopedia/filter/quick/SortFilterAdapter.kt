package com.tokopedia.filter.quick

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

internal class SortFilterAdapter(
    private val listener: SortFilterItemViewHolder.Listener,
): ListAdapter<SortFilterItem, SortFilterItemViewHolder>(
    SortFilterItemDiffUtilItemCallback()
) {
    companion object{
        const val VIEW_TYPE_IMAGE = 1
        const val VIEW_TYPE_COMMON = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortFilterItemViewHolder {
        return when(viewType){
            VIEW_TYPE_IMAGE -> createImageViewHolder(parent)
            else -> createCommonViewHolder(parent)
        }
    }

    private fun createImageViewHolder(parent: ViewGroup): SortFilterItemViewHolder =
        SortFilterItemImageViewHolder(
            LayoutInflater.from(parent.context).inflate(SortFilterItemImageViewHolder.LAYOUT, null),
            listener,
        )

    private fun createCommonViewHolder(parent: ViewGroup): SortFilterItemViewHolder =
        SortFilterItemCommonViewHolder(
            LayoutInflater.from(parent.context).inflate(SortFilterItemCommonViewHolder.LAYOUT, null),
            listener,
        )

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when{
            item.shouldShowImage -> VIEW_TYPE_IMAGE
            else -> VIEW_TYPE_COMMON
        }
    }
    override fun onBindViewHolder(holder: SortFilterItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
