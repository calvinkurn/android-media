package com.tokopedia.feedplus.browse.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChannelViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowsePlaceholderViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChannelBinding
import com.tokopedia.feedplus.databinding.ItemFeedBrowsePlaceholderBinding

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseAdapterDelegate private constructor() {

    internal class Placeholder:
        TypedAdapterDelegate<FeedBrowseUiModel.Placeholder, FeedBrowseUiModel, FeedBrowsePlaceholderViewHolder>(
            com.tokopedia.feedplus.R.layout.item_feed_browse_placeholder
        ) {

        override fun onBindViewHolder(
            item: FeedBrowseUiModel.Placeholder,
            holder: FeedBrowsePlaceholderViewHolder
        ) {
            holder.bind(item.type)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): FeedBrowsePlaceholderViewHolder {
            return FeedBrowsePlaceholderViewHolder(
                ItemFeedBrowsePlaceholderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                )
            )
        }
    }

    internal class Channel(
        private val listener: FeedBrowseChannelViewHolder.Listener
    ):
        TypedAdapterDelegate<FeedBrowseUiModel.Channel, FeedBrowseUiModel, FeedBrowseChannelViewHolder>(
            com.tokopedia.feedplus.R.layout.item_feed_browse_channel
        ) {

        override fun onBindViewHolder(
            item: FeedBrowseUiModel.Channel,
            holder: FeedBrowseChannelViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): FeedBrowseChannelViewHolder {
            return FeedBrowseChannelViewHolder(
                ItemFeedBrowseChannelBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                listener
            )
        }
    }
}
