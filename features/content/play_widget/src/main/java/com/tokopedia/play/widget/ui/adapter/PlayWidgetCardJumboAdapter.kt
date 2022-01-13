package com.tokopedia.play.widget.ui.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.jumbo.PlayWidgetCardJumboBannerAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.jumbo.PlayWidgetCardJumboChannelAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetJumboChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetJumboItemUiModel

/**
 * @author by astidhiyaa on 12/01/22
 */
class PlayWidgetCardJumboAdapter(
    channelCardListener: PlayWidgetCardJumboChannelViewHolder.Listener,
    bannerCardListener: PlayWidgetCardJumboBannerViewHolder.Listener
) :
    BaseDiffUtilAdapter<PlayWidgetJumboItemUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayWidgetCardJumboBannerAdapterDelegate(bannerCardListener))
            .addDelegate(PlayWidgetCardJumboChannelAdapterDelegate(channelCardListener))
    }

    override fun areItemsTheSame(
        oldItem: PlayWidgetJumboItemUiModel,
        newItem: PlayWidgetJumboItemUiModel
    ): Boolean {
        return if (oldItem is PlayWidgetJumboChannelUiModel && newItem is PlayWidgetJumboChannelUiModel) oldItem.channelId == newItem.channelId
        else oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: PlayWidgetJumboItemUiModel,
        newItem: PlayWidgetJumboItemUiModel
    ): Boolean = (oldItem == newItem)
}