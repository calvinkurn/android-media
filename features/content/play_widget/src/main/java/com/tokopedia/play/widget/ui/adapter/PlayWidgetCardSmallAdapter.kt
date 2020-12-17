package com.tokopedia.play.widget.ui.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.small.PlayWidgetCardSmallBannerAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.small.PlayWidgetCardSmallChannelAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.small.PlayWidgetCardSmallBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.small.PlayWidgetCardSmallChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallItemUiModel

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetCardSmallAdapter(
        bannerCardListener: PlayWidgetCardSmallBannerViewHolder.Listener,
        channelCardListener: PlayWidgetCardSmallChannelViewHolder.Listener
) : BaseDiffUtilAdapter<PlayWidgetSmallItemUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayWidgetCardSmallBannerAdapterDelegate(bannerCardListener))
                .addDelegate(PlayWidgetCardSmallChannelAdapterDelegate(channelCardListener))
    }

    override fun areItemsTheSame(oldItem: PlayWidgetSmallItemUiModel, newItem: PlayWidgetSmallItemUiModel): Boolean {
        return if (oldItem is PlayWidgetSmallChannelUiModel && newItem is PlayWidgetSmallChannelUiModel) oldItem.channelId == newItem.channelId
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayWidgetSmallItemUiModel, newItem: PlayWidgetSmallItemUiModel): Boolean {
        return oldItem == newItem
    }
}