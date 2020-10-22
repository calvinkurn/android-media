package com.tokopedia.play.widget.ui.adapter

import android.os.Bundle
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumBannerAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumChannelAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumOverlayAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumItemUiModel


/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetCardMediumAdapter(
        channelCardListener: PlayWidgetCardMediumChannelViewHolder.Listener
) : BaseDiffUtilAdapter<PlayWidgetMediumItemUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayWidgetCardMediumOverlayAdapterDelegate())
                .addDelegate(PlayWidgetCardMediumChannelAdapterDelegate(channelCardListener))
                .addDelegate(PlayWidgetCardMediumBannerAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: PlayWidgetMediumItemUiModel, newItem: PlayWidgetMediumItemUiModel): Boolean {
        return if (oldItem is PlayWidgetMediumChannelUiModel && newItem is PlayWidgetMediumChannelUiModel) oldItem.channelId == newItem.channelId
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayWidgetMediumItemUiModel, newItem: PlayWidgetMediumItemUiModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: PlayWidgetMediumItemUiModel, newItem: PlayWidgetMediumItemUiModel): Bundle? {
        val diffBundle = Bundle()
        if (oldItem is PlayWidgetMediumChannelUiModel && newItem is PlayWidgetMediumChannelUiModel) {
            diffBundle.putBoolean(PlayWidgetCardMediumChannelAdapterDelegate.KEY_CHANNEL_REMINDER, newItem.activeReminder)
        }
        return if (diffBundle.size() == 0) null else diffBundle
    }
}