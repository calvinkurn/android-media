package com.tokopedia.feedplus.browse.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseCardViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseCardAdapter(
    private val listener: FeedBrowseCardViewHolder.Listener
) : ListAdapter<PlayWidgetChannelUiModel, FeedBrowseCardViewHolder>(
    object : DiffUtil.ItemCallback<PlayWidgetChannelUiModel>() {
        override fun areItemsTheSame(
            oldItem: PlayWidgetChannelUiModel,
            newItem: PlayWidgetChannelUiModel
        ): Boolean {
            return oldItem.channelId == newItem.channelId
        }

        override fun areContentsTheSame(
            oldItem: PlayWidgetChannelUiModel,
            newItem: PlayWidgetChannelUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedBrowseCardViewHolder {
        return FeedBrowseCardViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: FeedBrowseCardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
