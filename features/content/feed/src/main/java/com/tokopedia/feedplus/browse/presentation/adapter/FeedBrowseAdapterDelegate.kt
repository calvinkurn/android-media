package com.tokopedia.feedplus.browse.presentation.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChannelViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowsePlaceholderViewHolder
import com.tokopedia.feedplus.browse.presentation.model.ChannelUiState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChannelBinding
import com.tokopedia.feedplus.databinding.ItemFeedBrowsePlaceholderBinding
import kotlinx.coroutines.CoroutineScope
import com.tokopedia.feedplus.R as feedplusR

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseAdapterDelegate private constructor() {

    internal class Placeholder :
        TypedAdapterDelegate<FeedBrowseUiModel.Placeholder, FeedBrowseUiModel, FeedBrowsePlaceholderViewHolder>(
            feedplusR.layout.item_feed_browse_placeholder
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
                    false
                )
            )
        }
    }

    internal class Channel(
        private val listener: FeedBrowseChannelViewHolder.Listener,
        private val lifeCycleScope: CoroutineScope,
        private val coroutineDispatchers: CoroutineDispatchers
    ) :
        TypedAdapterDelegate<FeedBrowseUiModel.Channel, FeedBrowseUiModel, FeedBrowseChannelViewHolder>(
            feedplusR.layout.item_feed_browse_channel
        ) {

        override fun onBindViewHolder(
            item: FeedBrowseUiModel.Channel,
            holder: FeedBrowseChannelViewHolder
        ) {
            holder.bind(item)
        }

        override fun onBindViewHolderWithPayloads(
            item: FeedBrowseUiModel.Channel,
            holder: FeedBrowseChannelViewHolder,
            payloads: Bundle
        ) {
            if (payloads.getBoolean(FeedBrowseChannelViewHolder.NOTIFY_CHIP_STATE)) {
                holder.bindChipUiState(item.chipUiState)
            }
            if (payloads.getBoolean(FeedBrowseChannelViewHolder.NOTIFY_CHANNEL_STATE)) {
                holder.bindChannelUiState(item.channelUiState, item)
            }
            if (payloads.getBoolean(FeedBrowseChannelViewHolder.NOTIFY_AUTO_REFRESH)) {
                if (item.channelUiState !is ChannelUiState.Data) return
                holder.configureAutoRefresh(item.channelUiState.config)
            }
            holder.updateItem(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): FeedBrowseChannelViewHolder {
            return FeedBrowseChannelViewHolder(
                binding = ItemFeedBrowseChannelBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener = listener,
                lifecycleScope = lifeCycleScope,
                coroutineDispatchers = coroutineDispatchers
            )
        }
    }
}
