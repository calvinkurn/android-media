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
            })
        }

        fun bind(data: PlayWidgetChannelUiModel) {
            itemView.addOnImpressionListener(data.impressHolder) {
                listener.onChannelImpressed(channelView, data, adapterPosition)
            }
            channelView.setModel(data)
        }

        fun bind(data: PlayWidgetChannelUiModel, payloads: Set<String>) {
            payloads.forEach {
                when (it) {
                    PlayWidgetCarouselDiffCallback.PAYLOAD_MUTE_CHANGE -> {
                        channelView.setMuted(shouldMuted = data.isMuted, animate = true)
//                        setMutedListener(data)
                    }
                    PlayWidgetCarouselDiffCallback.PAYLOAD_SELECTED -> {
                        channelView.showMuteButton(shouldShow = true)
                    }
                    PlayWidgetCarouselDiffCallback.PAYLOAD_NOT_SELECTED -> {
                        channelView.showMuteButton(shouldShow = false)
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
            })
        }

        fun bind(data: PlayWidgetChannelUiModel) {
            itemView.addOnImpressionListener(data.impressHolder) {
                listener.onChannelImpressed(upcomingView, data, adapterPosition)
            }
            upcomingView.setModel(data)
        }

        fun bind(data: PlayWidgetChannelUiModel, payloads: Set<String>) {
            payloads.forEach {
                when (it) {
                    PlayWidgetCarouselDiffCallback.PAYLOAD_REMINDED_CHANGE -> {
                        upcomingView.setReminded(data.reminderType.reminded, animate = true)
//                        setRemindedListener(data)
                    }
                    PlayWidgetCarouselDiffCallback.PAYLOAD_SELECTED -> {
                        upcomingView.showReminderButton(shouldShow = true)
                    }
                    PlayWidgetCarouselDiffCallback.PAYLOAD_NOT_SELECTED -> {
                        upcomingView.showReminderButton(shouldShow = false)
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
        }
    }
}
