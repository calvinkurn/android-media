package com.tokopedia.play.widget.ui.widget.large.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * Created by meyta.taliti on 29/01/22.
 */
class PlayWidgetLargeAdapter(
    cardChannelListener: PlayWidgetLargeViewHolder.Channel.Listener,
    cardBannerListener: PlayWidgetLargeViewHolder.Banner.Listener,
    cardTranscodeListener: PlayWidgetLargeViewHolder.Transcode.Listener,
) : BaseDiffUtilAdapter<Any>() {

    init {
        delegatesManager
            .addDelegate(PlayWidgetLargeAdapterDelegate.Banner(cardBannerListener))
            .addDelegate(PlayWidgetLargeAdapterDelegate.Channel(cardChannelListener))
            .addDelegate(PlayWidgetLargeAdapterDelegate.Transcode(cardTranscodeListener))
    }

    override fun areItemsTheSame(
        oldItem: Any,
        newItem: Any
    ): Boolean {
        return if (oldItem is PlayWidgetChannelUiModel && newItem is PlayWidgetChannelUiModel) {
            oldItem.channelId == newItem.channelId
        } else oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Any,
        newItem: Any
    ): Boolean {
        return oldItem == newItem
    }
}
