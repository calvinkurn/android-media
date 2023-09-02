package com.tokopedia.feedplus.browse.presentation.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.delegate.CardAdapterDelegate
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.FeedBrowseCardViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseCardAdapter(
    listener: FeedBrowseCardViewHolder.Listener
) : BaseDiffUtilAdapter<PlayWidgetChannelUiModel>() {

    init {
        delegatesManager.addDelegate(CardAdapterDelegate(listener))
    }

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
