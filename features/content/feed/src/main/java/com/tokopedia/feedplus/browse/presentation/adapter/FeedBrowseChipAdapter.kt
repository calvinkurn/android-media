package com.tokopedia.feedplus.browse.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChannelViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChipViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal class FeedBrowseChipAdapter(
    private val listener: FeedBrowseChipViewHolder.Listener
) : ListAdapter<FeedBrowseItemListModel.Chips.Model, FeedBrowseChipViewHolder>(
    object : DiffUtil.ItemCallback<FeedBrowseItemListModel.Chips.Model>() {
        override fun areItemsTheSame(
            oldItem: FeedBrowseItemListModel.Chips.Model,
            newItem: FeedBrowseItemListModel.Chips.Model
        ): Boolean {
            return oldItem.menu.id == newItem.menu.id
        }

        override fun areContentsTheSame(
            oldItem: FeedBrowseItemListModel.Chips.Model,
            newItem: FeedBrowseItemListModel.Chips.Model
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItem: FeedBrowseItemListModel.Chips.Model,
            newItem: FeedBrowseItemListModel.Chips.Model
        ): Any? {
            val payloadBuilder = FeedBrowsePayloads.Builder()
            if (oldItem.isSelected != newItem.isSelected) payloadBuilder.addSelectedChipChanged()
            return payloadBuilder.build()
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
