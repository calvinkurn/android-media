package com.tokopedia.play.widget.ui.adapter

import android.os.Bundle
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumBannerAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumChannelAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumOverlayAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.delegate.medium.PlayWidgetCardMediumTranscodeAdapterDelegate
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumBannerViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumChannelViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumOverlayViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.medium.PlayWidgetCardMediumTranscodeViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumItemUiModel
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
) : BaseDiffUtilAdapter<PlayWidgetMediumItemUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayWidgetCardMediumOverlayAdapterDelegate(overlayCardListener))
                .addDelegate(PlayWidgetCardMediumChannelAdapterDelegate(channelCardListener))
                .addDelegate(PlayWidgetCardMediumBannerAdapterDelegate(bannerCardListener))
                .addDelegate(PlayWidgetCardMediumTranscodeAdapterDelegate(imageBlurUtil, transcodeCardListener))
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
            if (oldItem.activeReminder != newItem.activeReminder) {
                diffBundle.putBoolean(PlayWidgetCardMediumChannelViewHolder.KEY_CHANNEL_REMINDER, newItem.activeReminder)
            }
            if (oldItem.totalView != newItem.totalView) {
                diffBundle.putString(PlayWidgetCardMediumChannelViewHolder.KEY_CHANNEL_TOTAL_VIEW, newItem.totalView)
            }
        }
        return if (diffBundle.size() == 0) null else diffBundle
    }
}