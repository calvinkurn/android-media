package com.tokopedia.play.widget.ui.widget.jumbo.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * Created by meyta.taliti on 29/01/22.
 */
class PlayWidgetJumboAdapter(
    cardChannelListener: PlayWidgetJumboViewHolder.Channel.Listener,
) : BaseDiffUtilAdapter<PlayWidgetItemUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayWidgetJumboAdapterDelegate.Channel(cardChannelListener))
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