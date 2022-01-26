package com.tokopedia.play.widget.ui.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.jumbo.PlayWidgetCardJumboBannerAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.jumbo.PlayWidgetCardJumboChannelAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.jumbo.PlayWidgetCardJumboTranscodeAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboChannelViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboTranscodeViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play_common.util.blur.ImageBlurUtil

/**
 * @author by astidhiyaa on 12/01/22
 */
class PlayWidgetCardJumboAdapter(
    imageBlurUtil: ImageBlurUtil,
    channelCardListener: PlayWidgetCardJumboChannelViewHolder.Listener,
    bannerCardListener: PlayWidgetCardJumboBannerViewHolder.Listener,
    transcodeListener: PlayWidgetCardJumboTranscodeViewHolder.Listener
) :
    BaseDiffUtilAdapter<PlayWidgetItemUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayWidgetCardJumboBannerAdapterDelegate(bannerCardListener))
            .addDelegate(PlayWidgetCardJumboChannelAdapterDelegate(channelCardListener))
            .addDelegate(PlayWidgetCardJumboTranscodeAdapterDelegate(imageBlurUtil, transcodeListener))
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