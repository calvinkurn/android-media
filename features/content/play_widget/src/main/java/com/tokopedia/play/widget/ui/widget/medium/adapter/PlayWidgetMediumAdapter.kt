package com.tokopedia.play.widget.ui.widget.medium.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play_common.util.blur.ImageBlurUtil

/**
 * Created by kenny.hadisaputra on 24/01/22
 */
internal class PlayWidgetMediumAdapter(
    imageBlurUtil: ImageBlurUtil,
    cardChannelListener: PlayWidgetMediumViewHolder.Channel.Listener,
    cardBannerListener: PlayWidgetMediumViewHolder.Banner.Listener,
) : BaseDiffUtilAdapter<PlayWidgetItemUiModel>() {

    init {
        delegatesManager
            .addDelegate(PlayWidgetMediumAdapterDelegate.Banner(cardBannerListener))
            .addDelegate(PlayWidgetMediumAdapterDelegate.Channel(cardChannelListener))
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