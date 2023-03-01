package com.tokopedia.play.widget.ui.widget.medium.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.widget.medium.model.PlayWidgetOverlayUiModel
import com.tokopedia.play_common.R as commonR

/**
 * Created by kenny.hadisaputra on 24/01/22
 */
internal class PlayWidgetMediumAdapterDelegate private constructor() {

    internal class Overlay(
        private val cardOverlayListener: PlayWidgetMediumViewHolder.Overlay.Listener
    ) : TypedAdapterDelegate<PlayWidgetOverlayUiModel, Any, PlayWidgetMediumViewHolder.Overlay>(commonR.layout.view_play_empty) {

        override fun onBindViewHolder(
            item: PlayWidgetOverlayUiModel,
            holder: PlayWidgetMediumViewHolder.Overlay
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetMediumViewHolder.Overlay {
            return PlayWidgetMediumViewHolder.Overlay.create(parent, cardOverlayListener)
        }
    }

    internal class Banner(
        private val cardBannerListener: PlayWidgetMediumViewHolder.Banner.Listener
    ) : TypedAdapterDelegate<
        PlayWidgetBannerUiModel, Any, PlayWidgetMediumViewHolder.Banner>(commonR.layout.view_play_empty) {

        override fun onBindViewHolder(
            item: PlayWidgetBannerUiModel,
            holder: PlayWidgetMediumViewHolder.Banner
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetMediumViewHolder.Banner {
            return PlayWidgetMediumViewHolder.Banner.create(parent, cardBannerListener)
        }
    }

    internal class Channel(
        private val cardChannelListener: PlayWidgetMediumViewHolder.Channel.Listener
    ) : BaseAdapterDelegate<PlayWidgetChannelUiModel, Any, ViewHolder>(commonR.layout.view_play_empty) {

        private val allowedTypes = listOf(
            PlayWidgetChannelType.Live,
            PlayWidgetChannelType.Vod,
            PlayWidgetChannelType.Upcoming,
            PlayWidgetChannelType.Unknown,
            PlayWidgetChannelType.Deleting
        )

        override fun onBindViewHolder(
            item: PlayWidgetChannelUiModel,
            holder: ViewHolder
        ) {
            if (holder is PlayWidgetMediumViewHolder.Channel) holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ViewHolder {
            return try {
                PlayWidgetMediumViewHolder.Channel.create(parent, cardChannelListener)
            } catch (e: Throwable) {
                object : ViewHolder(basicView) {}
            }
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
        private val cardTranscodeListener: PlayWidgetMediumViewHolder.Transcode.Listener
    ) : BaseAdapterDelegate<PlayWidgetChannelUiModel, Any, PlayWidgetMediumViewHolder.Transcode>(commonR.layout.view_play_empty) {

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
            holder: PlayWidgetMediumViewHolder.Transcode
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetMediumViewHolder.Transcode {
            return PlayWidgetMediumViewHolder.Transcode.create(
                parent,
                cardTranscodeListener
            )
        }
    }
}
