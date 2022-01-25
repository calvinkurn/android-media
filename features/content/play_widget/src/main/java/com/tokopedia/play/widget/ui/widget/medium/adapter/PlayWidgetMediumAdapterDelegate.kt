package com.tokopedia.play.widget.ui.widget.medium.adapter

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseAdapterDelegate
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType

/**
 * Created by kenny.hadisaputra on 24/01/22
 */
internal class PlayWidgetMediumAdapterDelegate {

    internal class Banner(
        private val cardBannerListener: PlayWidgetMediumViewHolder.Banner.Listener,
    ) : TypedAdapterDelegate<
            PlayWidgetBannerUiModel, PlayWidgetItemUiModel, PlayWidgetMediumViewHolder.Banner>(R.layout.view_empty) {

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
        private val cardChannelListener: PlayWidgetMediumViewHolder.Channel.Listener,
    ) : BaseAdapterDelegate<PlayWidgetChannelUiModel, PlayWidgetItemUiModel, PlayWidgetMediumViewHolder.Channel>(R.layout.view_empty) {

        private val allowedTypes = listOf(
            PlayWidgetChannelType.Live,
            PlayWidgetChannelType.Vod,
            PlayWidgetChannelType.Upcoming,
            PlayWidgetChannelType.Unknown,
            PlayWidgetChannelType.Deleting
        )

        override fun onBindViewHolder(
            item: PlayWidgetChannelUiModel,
            holder: PlayWidgetMediumViewHolder.Channel
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetMediumViewHolder.Channel {
            return PlayWidgetMediumViewHolder.Channel.create(parent, cardChannelListener)
        }

        override fun isForViewType(
            itemList: List<PlayWidgetItemUiModel>,
            position: Int,
            isFlexibleType: Boolean
        ): Boolean {
            val item = itemList[position]
            return if (item is PlayWidgetChannelUiModel) item.widgetType in allowedTypes
            else false
        }
    }

    internal class Transcode(
        private val cardTranscodeListener: PlayWidgetMediumViewHolder.Transcode.Listener,
    ) : BaseAdapterDelegate<PlayWidgetChannelUiModel, PlayWidgetItemUiModel, PlayWidgetMediumViewHolder.Transcode>(R.layout.view_empty) {

        private val allowedTypes = listOf(PlayWidgetChannelType.Transcoding, PlayWidgetChannelType.FailedTranscoding)

        override fun isForViewType(
            itemList: List<PlayWidgetItemUiModel>,
            position: Int,
            isFlexibleType: Boolean
        ): Boolean {
            val item = itemList[position]
            return if (item is PlayWidgetChannelUiModel) item.widgetType in allowedTypes
            else false
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