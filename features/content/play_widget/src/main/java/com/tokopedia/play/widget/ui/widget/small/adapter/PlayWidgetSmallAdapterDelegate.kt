package com.tokopedia.play.widget.ui.widget.small.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play_common.R as commonR
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * Created by kenny.hadisaputra on 24/01/22
 */
internal class PlayWidgetSmallAdapterDelegate {

    internal class Banner(
        private val cardBannerListener: PlayWidgetSmallViewHolder.Banner.Listener,
    ) : TypedAdapterDelegate<PlayWidgetBannerUiModel, PlayWidgetItemUiModel, PlayWidgetSmallViewHolder.Banner>(commonR.layout.view_play_empty) {

        override fun onBindViewHolder(
            item: PlayWidgetBannerUiModel,
            holder: PlayWidgetSmallViewHolder.Banner
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetSmallViewHolder.Banner {
            return PlayWidgetSmallViewHolder.Banner.create(parent, cardBannerListener)
        }
    }

    internal class Channel(
        private val cardChannelListener: PlayWidgetSmallViewHolder.Channel.Listener,
    ) : TypedAdapterDelegate<PlayWidgetChannelUiModel, PlayWidgetItemUiModel, PlayWidgetSmallViewHolder.Channel>(commonR.layout.view_play_empty) {

        override fun onBindViewHolder(
            item: PlayWidgetChannelUiModel,
            holder: PlayWidgetSmallViewHolder.Channel
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetSmallViewHolder.Channel {
            return PlayWidgetSmallViewHolder.Channel.create(parent, cardChannelListener)
        }
    }
}