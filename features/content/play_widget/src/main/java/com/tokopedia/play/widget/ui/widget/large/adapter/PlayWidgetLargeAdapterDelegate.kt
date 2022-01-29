package com.tokopedia.play.widget.ui.widget.large.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * Created by meyta.taliti on 29/01/22.
 */
class PlayWidgetLargeAdapterDelegate {

    internal class Banner(
        private val cardBannerListener: PlayWidgetLargeViewHolder.Banner.Listener,
    ) : TypedAdapterDelegate<PlayWidgetBannerUiModel, PlayWidgetItemUiModel, PlayWidgetLargeViewHolder.Banner>(
        R.layout.view_empty
    ) {

        override fun onBindViewHolder(
            item: PlayWidgetBannerUiModel,
            holder: PlayWidgetLargeViewHolder.Banner
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetLargeViewHolder.Banner {
            return PlayWidgetLargeViewHolder.Banner.create(parent, cardBannerListener)
        }
    }

    internal class Channel(
        private val cardChannelListener: PlayWidgetLargeViewHolder.Channel.Listener,
    ) : TypedAdapterDelegate<PlayWidgetChannelUiModel, PlayWidgetItemUiModel, PlayWidgetLargeViewHolder.Channel>(
        R.layout.view_empty
    ) {

        override fun onBindViewHolder(
            item: PlayWidgetChannelUiModel,
            holder: PlayWidgetLargeViewHolder.Channel
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetLargeViewHolder.Channel {
            return PlayWidgetLargeViewHolder.Channel.create(parent, cardChannelListener)
        }
    }
}