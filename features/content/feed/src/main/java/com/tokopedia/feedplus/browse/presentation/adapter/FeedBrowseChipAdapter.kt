package com.tokopedia.feedplus.browse.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChipViewHolder

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal class FeedBrowseChipAdapter(
    private val listener: FeedBrowseChipViewHolder.Listener
) : ListAdapter<WidgetMenuModel, FeedBrowseChipViewHolder>(
    object : DiffUtil.ItemCallback<WidgetMenuModel>() {
        override fun areItemsTheSame(
            oldItem: WidgetMenuModel,
            newItem: WidgetMenuModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: WidgetMenuModel,
            newItem: WidgetMenuModel
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedBrowseChipViewHolder {
        return FeedBrowseChipViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: FeedBrowseChipViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
