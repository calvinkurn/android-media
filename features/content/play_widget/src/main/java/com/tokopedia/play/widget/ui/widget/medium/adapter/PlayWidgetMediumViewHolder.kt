package com.tokopedia.play.widget.ui.widget.medium.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.widget.medium.PlayWidgetCardMediumChannelView
import com.tokopedia.play.widget.ui.widget.medium.PlayWidgetCardMediumBannerView

/**
 * Created by kenny.hadisaputra on 24/01/22
 */
class PlayWidgetMediumViewHolder {

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
        listener: Listener,
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
}