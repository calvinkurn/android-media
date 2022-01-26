package com.tokopedia.play.widget.ui.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.large.PlayWidgetCardLargeBannerAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.large.PlayWidgetCardLargeChannelAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.large.PlayWidgetCardLargeBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.large.PlayWidgetCardLargeChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * @author by astidhiyaa on 11/01/22
 */
class PlayWidgetCardLargeAdapter(
    channelCardListener: PlayWidgetCardLargeChannelViewHolder.Listener,
    bannerCardListener: PlayWidgetCardLargeBannerViewHolder.Listener,
) :
    BaseDiffUtilAdapter<PlayWidgetItemUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayWidgetCardLargeBannerAdapterDelegate(bannerCardListener))
            .addDelegate(PlayWidgetCardLargeChannelAdapterDelegate(channelCardListener))
    }

    override fun areItemsTheSame(
        oldItem: PlayWidgetItemUiModel,
        newItem: PlayWidgetItemUiModel
    ): Boolean {
        return if (oldItem is PlayWidgetChannelUiModel && newItem is PlayWidgetChannelUiModel) oldItem.channelId == newItem.channelId
        else oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: PlayWidgetItemUiModel,
        newItem: PlayWidgetItemUiModel
    ): Boolean = (oldItem == newItem)
}