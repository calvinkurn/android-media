package com.tokopedia.play.widget.ui.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.large.PlayWidgetCardLargeBannerAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.large.PlayWidgetCardLargeChannelAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.large.PlayWidgetCardLargeBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.large.PlayWidgetCardLargeChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetLargeChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetLargeItemUiModel

/**
 * @author by astidhiyaa on 11/01/22
 */
class PlayWidgetCardLargeAdapter(
    channelCardListener: PlayWidgetCardLargeChannelViewHolder.Listener,
    bannerCardListener: PlayWidgetCardLargeBannerViewHolder.Listener
) :
    BaseDiffUtilAdapter<PlayWidgetLargeItemUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayWidgetCardLargeBannerAdapterDelegate(bannerCardListener))
            .addDelegate(PlayWidgetCardLargeChannelAdapterDelegate(channelCardListener))
    }

    override fun areItemsTheSame(
        oldItem: PlayWidgetLargeItemUiModel,
        newItem: PlayWidgetLargeItemUiModel
    ): Boolean {
        return if (oldItem is PlayWidgetLargeChannelUiModel && newItem is PlayWidgetLargeChannelUiModel) oldItem.channelId == newItem.channelId
        else oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: PlayWidgetLargeItemUiModel,
        newItem: PlayWidgetLargeItemUiModel
    ): Boolean = (oldItem == newItem)
}