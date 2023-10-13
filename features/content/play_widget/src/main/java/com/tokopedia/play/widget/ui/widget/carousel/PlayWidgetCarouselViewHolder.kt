package com.tokopedia.play.widget.ui.widget.carousel

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.ext.isMuted
import com.tokopedia.play.widget.ui.model.reminded

/**
 * Created by kenny.hadisaputra on 17/05/23
 */
class PlayWidgetCarouselViewHolder private constructor() {

    class VideoContent private constructor(
        private val channelView: PlayWidgetCardCarouselChannelView,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(channelView) {

        init {
            channelView.setListener(object : PlayWidgetCardCarouselChannelView.Listener {
                override fun onMuteButtonClicked(
                    view: PlayWidgetCardCarouselChannelView,
                    item: PlayWidgetChannelUiModel,
                    shouldMute: Boolean
                ) {
                    listener.onMuteButtonClicked(view, item, shouldMute, absoluteAdapterPosition)
                }

                override fun onProductImpressed(
                    view: PlayWidgetCardCarouselChannelView,
                    item: PlayWidgetChannelUiModel,
                    product: PlayWidgetProduct,
                    position: Int
                ) {
                    listener.onProductImpressed(view, item, product, position, absoluteAdapterPosition)
                }

                override fun onProductClicked(
                    view: PlayWidgetCardCarouselChannelView,
                    item: PlayWidgetChannelUiModel,
                    product: PlayWidgetProduct,
                    position: Int
                ) {
                    listener.onProductClicked(view, item, product, position, absoluteAdapterPosition)
                }

                override fun onPartnerClicked(
                    view: PlayWidgetCardCarouselChannelView,
                    item: PlayWidgetChannelUiModel
                ) {
                    listener.onPartnerClicked(view, item, absoluteAdapterPosition)
                }

                override fun onOverlayClicked(
                    view: PlayWidgetCardCarouselChannelView,
                    item: PlayWidgetChannelUiModel
                ) {
                    listener.onOverlayClicked(view, item, absoluteAdapterPosition)
                }
            })
        }

        internal fun bind(data: PlayWidgetCarouselAdapter.Model) {
            itemView.setOnClickListener {
                listener.onChannelClicked(channelView, data.channel, absoluteAdapterPosition)
            }
            itemView.addOnImpressionListener(data.channel.impressHolder) {
                listener.onChannelImpressed(channelView, data.channel, absoluteAdapterPosition)
            }
            channelView.setModel(data.channel)
            channelView.showMuteButton(data.isSelected)
            if (!data.isSelected) channelView.resetProductPosition()
            channelView.setShowOverlay(!data.isSelected)
        }

        internal fun bind(data: PlayWidgetCarouselAdapter.Model, payloads: Set<String>) {
            channelView.setModel(data.channel, invalidate = false)
            payloads.forEach {
                when (it) {
                    PlayWidgetCarouselDiffCallback.PAYLOAD_MUTE_CHANGE -> {
                        channelView.setMuted(
                            shouldMuted = data.channel.isMuted,
                            animate = data.isSelected
                        )
                    }
                    PlayWidgetCarouselDiffCallback.PAYLOAD_SELECTED_CHANGE -> {
                        channelView.showMuteButton(data.isSelected)
                        if (!data.isSelected) channelView.resetProductPosition()
                        channelView.setShowOverlay(!data.isSelected)
                    }
                    PlayWidgetCarouselDiffCallback.PAYLOAD_TOTAL_VIEW_CHANGE -> {
                        channelView.updateTotalView(data.channel.totalView.totalViewFmt)
                    }
                }
            }
        }

        internal fun onRecycled() {
            channelView.setPlayer(null)
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener
            ) = VideoContent(
                PlayWidgetCardCarouselChannelView(parent.context),
                listener
            )
        }

        interface Listener {
            fun onChannelImpressed(
                view: PlayWidgetCardCarouselChannelView,
                item: PlayWidgetChannelUiModel,
                position: Int
            )

            fun onChannelClicked(
                view: PlayWidgetCardCarouselChannelView,
                item: PlayWidgetChannelUiModel,
                position: Int
            )

            fun onMuteButtonClicked(
                view: PlayWidgetCardCarouselChannelView,
                item: PlayWidgetChannelUiModel,
                shouldMute: Boolean,
                position: Int
            )

            fun onProductImpressed(
                view: PlayWidgetCardCarouselChannelView,
                item: PlayWidgetChannelUiModel,
                product: PlayWidgetProduct,
                productPosition: Int,
                position: Int
            )

            fun onProductClicked(
                view: PlayWidgetCardCarouselChannelView,
                item: PlayWidgetChannelUiModel,
                product: PlayWidgetProduct,
                productPosition: Int,
                position: Int
            )

            fun onPartnerClicked(
                view: PlayWidgetCardCarouselChannelView,
                item: PlayWidgetChannelUiModel,
                position: Int
            )

            fun onOverlayClicked(
                view: PlayWidgetCardCarouselChannelView,
                item: PlayWidgetChannelUiModel,
                position: Int
            )
        }

        interface DataSource {
            fun canAutoPlay(item: PlayWidgetChannelUiModel): Boolean
        }
    }

    class UpcomingContent private constructor(
        private val upcomingView: PlayWidgetCardCarouselUpcomingView,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(upcomingView) {

        init {
            upcomingView.setListener(object : PlayWidgetCardCarouselUpcomingView.Listener {
                override fun onReminderClicked(
                    view: PlayWidgetCardCarouselUpcomingView,
                    item: PlayWidgetChannelUiModel,
                    reminderType: PlayWidgetReminderType
                ) {
                    listener.onReminderClicked(
                        view,
                        item,
                        reminderType,
                        absoluteAdapterPosition
                    )
                }

                override fun onPartnerClicked(
                    view: PlayWidgetCardCarouselUpcomingView,
                    item: PlayWidgetChannelUiModel
                ) {
                    listener.onPartnerClicked(view, item, absoluteAdapterPosition)
                }

                override fun onOverlayClicked(
                    view: PlayWidgetCardCarouselUpcomingView,
                    item: PlayWidgetChannelUiModel
                ) {
                    listener.onOverlayClicked(view, item, absoluteAdapterPosition)
                }
            })
        }

        internal fun bind(data: PlayWidgetCarouselAdapter.Model) {
            itemView.setOnClickListener {
                listener.onChannelClicked(upcomingView, data.channel, absoluteAdapterPosition)
            }
            itemView.addOnImpressionListener(data.channel.impressHolder) {
                listener.onChannelImpressed(upcomingView, data.channel, absoluteAdapterPosition)
            }
            upcomingView.setModel(data.channel)
            upcomingView.showReminderButton(data.isSelected)
            upcomingView.setShowOverlay(!data.isSelected)
        }

        internal fun bind(data: PlayWidgetCarouselAdapter.Model, payloads: Set<String>) {
            upcomingView.setModel(data.channel, invalidate = false)
            payloads.forEach {
                when (it) {
                    PlayWidgetCarouselDiffCallback.PAYLOAD_REMINDED_CHANGE -> {
                        upcomingView.setReminded(
                            data.channel.reminderType.reminded,
                            animate = data.isSelected
                        )
                    }
                    PlayWidgetCarouselDiffCallback.PAYLOAD_SELECTED_CHANGE -> {
                        upcomingView.showReminderButton(data.isSelected)
                        upcomingView.setShowOverlay(!data.isSelected)
                    }
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener
            ) = UpcomingContent(
                PlayWidgetCardCarouselUpcomingView(parent.context),
                listener
            )
        }

        interface Listener {
            fun onChannelImpressed(
                view: PlayWidgetCardCarouselUpcomingView,
                item: PlayWidgetChannelUiModel,
                position: Int
            )

            fun onChannelClicked(
                view: PlayWidgetCardCarouselUpcomingView,
                item: PlayWidgetChannelUiModel,
                position: Int
            )

            fun onReminderClicked(
                view: PlayWidgetCardCarouselUpcomingView,
                item: PlayWidgetChannelUiModel,
                reminderType: PlayWidgetReminderType,
                position: Int
            )

            fun onPartnerClicked(
                view: PlayWidgetCardCarouselUpcomingView,
                item: PlayWidgetChannelUiModel,
                position: Int
            )

            fun onOverlayClicked(
                view: PlayWidgetCardCarouselUpcomingView,
                item: PlayWidgetChannelUiModel,
                position: Int
            )
        }
    }
}
