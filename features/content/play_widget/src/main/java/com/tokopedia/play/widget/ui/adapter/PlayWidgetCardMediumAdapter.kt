package com.tokopedia.play.widget.ui.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumBannerAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumChannelAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumOverlayAdapterDelegate
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumItemUiModel


/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetCardMediumAdapter(
        cardMediumListener: CardMediumListener
) : BaseDiffUtilAdapter<PlayWidgetMediumItemUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayWidgetCardMediumOverlayAdapterDelegate(cardMediumListener))
                .addDelegate(PlayWidgetCardMediumChannelAdapterDelegate(cardMediumListener))
                .addDelegate(PlayWidgetCardMediumBannerAdapterDelegate(cardMediumListener))
    }

    override fun areItemsTheSame(oldItem: PlayWidgetMediumItemUiModel, newItem: PlayWidgetMediumItemUiModel): Boolean {
        return if (oldItem is PlayWidgetMediumChannelUiModel && newItem is PlayWidgetMediumChannelUiModel) oldItem.channelId == newItem.channelId
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayWidgetMediumItemUiModel, newItem: PlayWidgetMediumItemUiModel): Boolean {
        return oldItem == newItem
    }

    interface CardMediumListener {
        fun onCardMediumClicked(item: PlayWidgetMediumItemUiModel, position: Int)
        fun onCardMediumVisible(item: PlayWidgetMediumItemUiModel, position: Int)
    }
}