package com.tokopedia.play.widget.ui.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.jumbo.PlayWidgetCardJumboBannerAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.jumbo.PlayWidgetCardJumboChannelAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboChannelViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * @author by astidhiyaa on 12/01/22
 */
class PlayWidgetCardJumboAdapter(
    channelCardListener: PlayWidgetCardJumboChannelViewHolder.Listener,
    bannerCardListener: PlayWidgetCardJumboBannerViewHolder.Listener,
) :
    BaseDiffUtilAdapter<PlayWidgetItemUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayWidgetCardJumboBannerAdapterDelegate(bannerCardListener))
            .addDelegate(PlayWidgetCardJumboChannelAdapterDelegate(channelCardListener))
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