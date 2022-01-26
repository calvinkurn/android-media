package com.tokopedia.play.widget.ui.widget.medium.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.widget.medium.PlayWidgetCardMediumChannelView
import com.tokopedia.play.widget.ui.widget.medium.PlayWidgetCardMediumBannerView
import com.tokopedia.play.widget.ui.widget.medium.PlayWidgetCardMediumTranscodeView
import com.tokopedia.play.widget.ui.widget.medium.model.PlayWidgetOverlayUiModel

/**
 * Created by kenny.hadisaputra on 24/01/22
 */
class PlayWidgetMediumViewHolder {

    internal class Overlay private constructor(
        itemView: View,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                listener.onOverlayClicked(it, adapterPosition)
            }
        }

        fun bind(item: PlayWidgetOverlayUiModel) {
            itemView.addOnImpressionListener(item.impressHolder) {
                listener.onOverlayImpressed(itemView, adapterPosition)
            }
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) = Overlay(
                itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_play_widget_card_medium_overlay, parent, false),
                listener = listener,
            )
        }

        internal interface Listener {
            fun onOverlayImpressed(
                view: View,
                position: Int
            )

            fun onOverlayClicked(
                view: View,
                position: Int
            )
        }
    }

    class Banner private constructor(
        itemView: View,
        listener: Listener,
    ) : RecyclerView.ViewHolder(itemView) {

        private val bannerView = itemView as PlayWidgetCardMediumBannerView
        private val cardBannerListener = object : PlayWidgetCardMediumBannerView.Listener {
            override fun onBannerClicked(view: View, item: PlayWidgetBannerUiModel) {
                listener.onBannerClicked(view, item, adapterPosition)
            }
        }

        init {
            bannerView.setListener(cardBannerListener)
        }

        fun bind(data: PlayWidgetBannerUiModel) {
            bannerView.setData(data)
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) = Banner(
                itemView = PlayWidgetCardMediumBannerView(parent.context),
                listener = listener,
            )
        }

        interface Listener {

            fun onBannerClicked(
                view: View,
                item: PlayWidgetBannerUiModel,
                position: Int,
            )
        }
    }

    class Channel private constructor(
        itemView: View,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(itemView) {

        private val channelView = itemView as PlayWidgetCardMediumChannelView
        private val cardChannelListener = object : PlayWidgetCardMediumChannelView.Listener {

            override fun onChannelClicked(view: View, item: PlayWidgetChannelUiModel) {
                listener.onChannelClicked(view, item, adapterPosition)
            }

            override fun onToggleReminderChannelClicked(item: PlayWidgetChannelUiModel, reminderType: PlayWidgetReminderType) {
                listener.onToggleReminderChannelClicked(item, reminderType, adapterPosition)
            }

            override fun onMenuActionButtonClicked(view: View, item: PlayWidgetChannelUiModel) {
                listener.onMenuActionButtonClicked(view, item, adapterPosition)
            }
        }

        init {
            channelView.setListener(cardChannelListener)
        }

        fun bind(data: PlayWidgetChannelUiModel) {
            itemView.addOnImpressionListener(data.impressHolder) {
                listener.onChannelImpressed(itemView, data, adapterPosition)
            }
            channelView.setData(data)
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener
            ) = Channel(
                itemView = PlayWidgetCardMediumChannelView(parent.context),
                listener = listener,
            )
        }

        interface Listener {

            fun onChannelImpressed(
                view: View,
                item: PlayWidgetChannelUiModel,
                position: Int
            )

            fun onChannelClicked(
                view: View,
                item: PlayWidgetChannelUiModel,
                position: Int
            )

            fun onToggleReminderChannelClicked(
                item: PlayWidgetChannelUiModel,
                reminderType: PlayWidgetReminderType,
                position: Int
            )

            fun onMenuActionButtonClicked(
                view: View,
                item: PlayWidgetChannelUiModel,
                position: Int
            )
        }
    }

    class Transcode private constructor(
        itemView: View,
        listener: Listener,
    ) : RecyclerView.ViewHolder(itemView) {

        private val transcodeView = itemView as PlayWidgetCardMediumTranscodeView

        init {
            transcodeView.setListener(object : PlayWidgetCardMediumTranscodeView.Listener {
                override fun onFailedTranscodingChannelDeleteButtonClicked(
                    view: View,
                    item: PlayWidgetChannelUiModel
                ) {
                    listener.onFailedTranscodingChannelDeleteButtonClicked(view, item, adapterPosition)
                }
            })
        }

        fun bind(data: PlayWidgetChannelUiModel) {
            transcodeView.setData(data)
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener
            ) = Transcode(
                itemView = PlayWidgetCardMediumTranscodeView(parent.context),
                listener = listener,
            )
        }

        interface Listener {

            fun onFailedTranscodingChannelDeleteButtonClicked(
                view: View,
                item: PlayWidgetChannelUiModel,
                position: Int,
            )
        }
    }
}