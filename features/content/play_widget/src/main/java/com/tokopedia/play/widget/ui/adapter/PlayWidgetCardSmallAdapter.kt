package com.tokopedia.play.widget.ui.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.small.PlayWidgetCardSmallBannerAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.small.PlayWidgetCardSmallChannelAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.small.PlayWidgetCardSmallBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.small.PlayWidgetCardSmallChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetCardSmallAdapter(
        bannerCardListener: PlayWidgetCardSmallBannerViewHolder.Listener,
        channelCardListener: PlayWidgetCardSmallChannelViewHolder.Listener
) : BaseDiffUtilAdapter<PlayWidgetItemUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayWidgetCardSmallBannerAdapterDelegate(bannerCardListener))
                .addDelegate(PlayWidgetCardSmallChannelAdapterDelegate(channelCardListener))
    }

    override fun areItemsTheSame(oldItem: PlayWidgetItemUiModel, newItem: PlayWidgetItemUiModel): Boolean {
        return if (oldItem is PlayWidgetChannelUiModel && newItem is PlayWidgetChannelUiModel) oldItem.channelId == newItem.channelId
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayWidgetItemUiModel, newItem: PlayWidgetItemUiModel): Boolean {
        return oldItem == newItem
    }
}