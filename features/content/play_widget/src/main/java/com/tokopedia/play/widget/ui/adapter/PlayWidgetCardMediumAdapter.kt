package com.tokopedia.play.widget.ui.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumBannerAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumChannelAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumOverlayAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumTranscodeAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumOverlayViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumTranscodeViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play_common.util.blur.ImageBlurUtil


/**
 * Created by mzennis on 07/10/20.
 */
class PlayWidgetCardMediumAdapter(
        imageBlurUtil: ImageBlurUtil,
        overlayCardListener: PlayWidgetCardMediumOverlayViewHolder.Listener,
        channelCardListener: PlayWidgetCardMediumChannelViewHolder.Listener,
        bannerCardListener: PlayWidgetCardMediumBannerViewHolder.Listener,
        transcodeCardListener: PlayWidgetCardMediumTranscodeViewHolder.Listener
) : BaseDiffUtilAdapter<PlayWidgetItemUiModel>() {

    init {
        delegatesManager
//                .addDelegate(PlayWidgetCardMediumOverlayAdapterDelegate(overlayCardListener))
                .addDelegate(PlayWidgetCardMediumChannelAdapterDelegate(channelCardListener))
                .addDelegate(PlayWidgetCardMediumBannerAdapterDelegate(bannerCardListener))
                .addDelegate(PlayWidgetCardMediumTranscodeAdapterDelegate(imageBlurUtil, transcodeCardListener))
    }

    override fun areItemsTheSame(oldItem: PlayWidgetItemUiModel, newItem: PlayWidgetItemUiModel): Boolean {
        return if (oldItem is PlayWidgetChannelUiModel && newItem is PlayWidgetChannelUiModel) oldItem.channelId == newItem.channelId
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayWidgetItemUiModel, newItem: PlayWidgetItemUiModel): Boolean {
        return oldItem == newItem
    }
}