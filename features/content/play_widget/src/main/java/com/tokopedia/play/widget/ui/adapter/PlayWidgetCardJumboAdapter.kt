package com.tokopedia.play.widget.ui.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.jumbo.PlayWidgetCardJumboBannerAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.jumbo.PlayWidgetCardJumboChannelAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.jumbo.PlayWidgetCardJumboTranscodeAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboChannelViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.jumbo.PlayWidgetCardJumboTranscodeViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetJumboChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetJumboItemUiModel
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
    BaseDiffUtilAdapter<PlayWidgetJumboItemUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayWidgetCardJumboBannerAdapterDelegate(bannerCardListener))
            .addDelegate(PlayWidgetCardJumboChannelAdapterDelegate(channelCardListener))
            .addDelegate(PlayWidgetCardJumboTranscodeAdapterDelegate(imageBlurUtil, transcodeListener))
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