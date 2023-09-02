package com.tokopedia.feedplus.browse.presentation.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.delegate.CardAdapterDelegate
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.widget.PlayWidgetCardView

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseCardAdapter(
    listener: PlayWidgetCardView.Listener
): BaseDiffUtilAdapter<PlayWidgetChannelUiModel>() {

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
