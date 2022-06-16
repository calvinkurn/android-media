package com.tokopedia.play.widget.ui.widget.small.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * Created by kenny.hadisaputra on 24/01/22
 */
internal class PlayWidgetSmallAdapter(
    cardChannelListener: PlayWidgetSmallViewHolder.Channel.Listener,
    cardBannerListener: PlayWidgetSmallViewHolder.Banner.Listener,
) : BaseDiffUtilAdapter<PlayWidgetItemUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayWidgetSmallAdapterDelegate.Banner(cardBannerListener))
            .addDelegate(PlayWidgetSmallAdapterDelegate.Channel(cardChannelListener))
    }

    override fun areItemsTheSame(
        oldItem: PlayWidgetItemUiModel,
        newItem: PlayWidgetItemUiModel
    ): Boolean {
        return if (oldItem is PlayWidgetChannelUiModel && newItem is PlayWidgetChannelUiModel) {
            oldItem.channelId == newItem.channelId
        } else oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: PlayWidgetItemUiModel,
        newItem: PlayWidgetItemUiModel
    ): Boolean {
        return oldItem == newItem
    }
}