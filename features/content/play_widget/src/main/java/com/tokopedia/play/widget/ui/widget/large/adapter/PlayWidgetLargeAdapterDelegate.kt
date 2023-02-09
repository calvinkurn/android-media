package com.tokopedia.play.widget.ui.widget.large.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play_common.R as commonR
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.widget.medium.adapter.PlayWidgetMediumViewHolder

/**
 * Created by meyta.taliti on 29/01/22.
 */
class PlayWidgetLargeAdapterDelegate {

    internal class Banner(
        private val cardBannerListener: PlayWidgetLargeViewHolder.Banner.Listener,
    ) : TypedAdapterDelegate<PlayWidgetBannerUiModel, Any, PlayWidgetLargeViewHolder.Banner>(
        commonR.layout.view_play_empty
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
    ) : BaseAdapterDelegate<PlayWidgetChannelUiModel, Any, PlayWidgetLargeViewHolder.Channel>(
        commonR.layout.view_play_empty
    ) {

        private val allowedTypes = listOf(
            PlayWidgetChannelType.Live,
            PlayWidgetChannelType.Vod,
            PlayWidgetChannelType.Upcoming,
            PlayWidgetChannelType.Unknown,
            PlayWidgetChannelType.Deleting
        )

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

        override fun isForViewType(
            itemList: List<Any>,
            position: Int,
            isFlexibleType: Boolean
        ): Boolean {
            val item = itemList[position]
            return if (item is PlayWidgetChannelUiModel) {
                item.channelType in allowedTypes
            } else {
                false
            }
        }
    }

    internal class Transcode(
        private val cardTranscodeListener: PlayWidgetLargeViewHolder.Transcode.Listener
    ) : BaseAdapterDelegate<PlayWidgetChannelUiModel, Any, PlayWidgetLargeViewHolder.Transcode>(commonR.layout.view_play_empty) {

        private val allowedTypes = listOf(PlayWidgetChannelType.Transcoding, PlayWidgetChannelType.FailedTranscoding)

        override fun isForViewType(
            itemList: List<Any>,
            position: Int,
            isFlexibleType: Boolean
        ): Boolean {
            val item = itemList[position]
            return if (item is PlayWidgetChannelUiModel) {
                item.channelType in allowedTypes
            } else {
                false
            }
        }

        override fun onBindViewHolder(
            item: PlayWidgetChannelUiModel,
            holder: PlayWidgetLargeViewHolder.Transcode
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetLargeViewHolder.Transcode {
            return PlayWidgetLargeViewHolder.Transcode.create(
                parent,
                cardTranscodeListener
            )
        }
    }
}
