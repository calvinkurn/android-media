package com.tokopedia.play.widget.ui.widget.medium.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * @author by astidhiyaa on 05/12/22
 */
class PlayWidgetChannelMediumAdapter(
    cardChannelListener: PlayWidgetMediumViewHolder.Channel.Listener,
) : BaseDiffUtilAdapter<Any>() {

    init {
        delegatesManager
            .addDelegate(PlayWidgetMediumAdapterDelegate.Channel(cardChannelListener))
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
