package com.tokopedia.feedplus.browse.presentation.adapter

import android.os.Bundle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseChannelViewHolder
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import kotlinx.coroutines.CoroutineScope

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseAdapter(
    channelListener: FeedBrowseChannelViewHolder.Listener,
    lifeCycleScope: CoroutineScope,
    coroutineDispatchers: CoroutineDispatchers
) : BaseDiffUtilAdapter<FeedBrowseUiModel>(isFlexibleType = true) {

    init {
        delegatesManager
            .addDelegate(FeedBrowseAdapterDelegate.Placeholder())
            .addDelegate(FeedBrowseAdapterDelegate.Channel(channelListener, lifeCycleScope, coroutineDispatchers))
    }

    override fun areItemsTheSame(oldItem: FeedBrowseUiModel, newItem: FeedBrowseUiModel): Boolean {
        return if (oldItem is FeedBrowseUiModel.Channel && newItem is FeedBrowseUiModel.Channel) {
            oldItem.id == newItem.id
        } else {
            oldItem == newItem
        }
    }

    override fun areContentsTheSame(
        oldItem: FeedBrowseUiModel,
        newItem: FeedBrowseUiModel
    ): Boolean {
        return newItem == oldItem
    }

    override fun getChangePayload(oldItem: FeedBrowseUiModel, newItem: FeedBrowseUiModel): Bundle? {
        return if (oldItem is FeedBrowseUiModel.Channel && newItem is FeedBrowseUiModel.Channel) {
            val bundle = Bundle()
            if (oldItem.chipUiState != newItem.chipUiState) {
                bundle.apply {
                    putBoolean(FeedBrowseChannelViewHolder.NOTIFY_CHIP_STATE, true)
                }
            }
            if (oldItem.channelUiState != newItem.channelUiState) {
                bundle.apply {
                    putBoolean(FeedBrowseChannelViewHolder.NOTIFY_CHANNEL_STATE, true)
                }
            }
            bundle.apply {
                putBoolean(FeedBrowseChannelViewHolder.NOTIFY_AUTO_REFRESH, true)
            }
        } else {
            super.getChangePayload(oldItem, newItem)
        }
    }
}
