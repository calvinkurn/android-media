package com.tokopedia.play.widget.ui.widget.carousel

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.ext.isMuted
import com.tokopedia.play.widget.ui.model.reminded
import kotlin.math.abs

/**
 * Created by kenny.hadisaputra on 17/05/23
 */
class PlayWidgetCarouselViewHolder private constructor() {

    class VideoContent private constructor(
        private val channelView: PlayWidgetCardCarouselChannelView,
        private val listener: Listener,
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

                override fun onProductClicked(
                    view: PlayWidgetCardCarouselChannelView,
                    product: PlayWidgetProduct
                ) {
                    listener.onProductClicked(view, product, absoluteAdapterPosition)
                }

                override fun onPartnerClicked(
                    view: PlayWidgetCardCarouselChannelView,
                    partner: PlayWidgetPartnerUiModel
                ) {
                    listener.onPartnerClicked(view, partner, absoluteAdapterPosition)
                }
            })
        }

        internal fun bind(data: PlayWidgetCarouselAdapter.Model) {
            itemView.addOnImpressionListener(data.channel.impressHolder) {
                listener.onChannelImpressed(channelView, data.channel, absoluteAdapterPosition)
            }
            channelView.setModel(data.channel)
            channelView.showMuteButton(data.isSelected)
            if (!data.isSelected) channelView.resetProductPosition()
        }

        internal fun bind(data: PlayWidgetCarouselAdapter.Model, payloads: Set<String>) {
            channelView.setModel(data.channel, invalidate = false)
            payloads.forEach {
                when (it) {
                    PlayWidgetCarouselDiffCallback.PAYLOAD_MUTE_CHANGE -> {
                        channelView.setMuted(
                            shouldMuted = data.channel.isMuted,
                            animate = data.isSelected,
                        )
                    }
                    PlayWidgetCarouselDiffCallback.PAYLOAD_SELECTED_CHANGE -> {
                        channelView.showMuteButton(data.isSelected)
                        if (!data.isSelected) channelView.resetProductPosition()
                    }
                    PlayWidgetCarouselDiffCallback.PAYLOAD_TOTAL_VIEW_CHANGE -> {
                        channelView.updateTotalView(data.channel.totalView.totalViewFmt)
                    }
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) = VideoContent(
                PlayWidgetCardCarouselChannelView(parent.context),
                listener,
            )
        }

        interface Listener {
            fun onChannelImpressed(
                view: PlayWidgetCardCarouselChannelView,
                item: PlayWidgetChannelUiModel,
                position: Int
            )

            fun onMuteButtonClicked(
                view: PlayWidgetCardCarouselChannelView,
                item: PlayWidgetChannelUiModel,
                shouldMute: Boolean,
                position: Int,
            )

            fun onProductClicked(
                view: PlayWidgetCardCarouselChannelView,
                product: PlayWidgetProduct,
                position: Int,
            )

            fun onPartnerClicked(
                view: PlayWidgetCardCarouselChannelView,
                partner: PlayWidgetPartnerUiModel,
                position: Int,
            )
        }
    }

    class UpcomingContent private constructor(
        private val upcomingView: PlayWidgetCardCarouselUpcomingView,
        private val listener: Listener,
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
                    partner: PlayWidgetPartnerUiModel
                ) {
                    listener.onPartnerClicked(view, partner, absoluteAdapterPosition)
                }
            })
        }

        internal fun bind(data: PlayWidgetCarouselAdapter.Model) {
            itemView.addOnImpressionListener(data.channel.impressHolder) {
                listener.onChannelImpressed(upcomingView, data.channel, absoluteAdapterPosition)
            }
            upcomingView.setModel(data.channel)
            upcomingView.showReminderButton(data.isSelected)
        }

        internal fun bind(data: PlayWidgetCarouselAdapter.Model, payloads: Set<String>) {
            upcomingView.setModel(data.channel, invalidate = false)
            payloads.forEach {
                when (it) {
                    PlayWidgetCarouselDiffCallback.PAYLOAD_REMINDED_CHANGE -> {
                        upcomingView.setReminded(
                            data.channel.reminderType.reminded,
                            animate = data.isSelected,
                        )
                    }
                    PlayWidgetCarouselDiffCallback.PAYLOAD_SELECTED_CHANGE -> {
                        upcomingView.showReminderButton(data.isSelected)
                    }
                }
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) = UpcomingContent(
                PlayWidgetCardCarouselUpcomingView(parent.context),
                listener,
            )
        }

        interface Listener {
            fun onChannelImpressed(
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
                partner: PlayWidgetPartnerUiModel,
                position: Int,
            )
        }
    }
}
