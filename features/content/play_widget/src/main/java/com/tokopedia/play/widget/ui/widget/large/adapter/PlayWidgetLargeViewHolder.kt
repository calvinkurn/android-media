package com.tokopedia.play.widget.ui.widget.large.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.widget.large.PlayWidgetCardLargeBannerView
import com.tokopedia.play.widget.ui.widget.large.PlayWidgetCardLargeChannelView

/**
 * Created by meyta.taliti on 29/01/22.
 */
class PlayWidgetLargeViewHolder {

    class Banner private constructor(
        itemView: View,
        val listener: Listener,
    ) : RecyclerView.ViewHolder(itemView) {

        private val bannerView = itemView as PlayWidgetCardLargeBannerView

        init {
            bannerView.setListener(object : PlayWidgetCardLargeBannerView.Listener {
                override fun onBannerClicked(view: View, item: PlayWidgetBannerUiModel) {
                    listener.onBannerClicked(view, item, adapterPosition)
                }
            })
        }

        fun bind(data: PlayWidgetBannerUiModel) {
            itemView.addOnImpressionListener(data.impressHolder) {
                listener.onBannerImpressed(itemView, data, adapterPosition)
            }
            bannerView.setData(data)
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) = Banner(
                itemView = PlayWidgetCardLargeBannerView(parent.context),
                listener = listener,
            )
        }

        interface Listener {

            fun onBannerImpressed(
                view: View,
                item: PlayWidgetBannerUiModel,
                position: Int
            )

            fun onBannerClicked(
                view: View,
                item: PlayWidgetBannerUiModel,
                position: Int
            )
        }
    }

    class Channel private constructor(
        itemView: View,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(itemView) {

        private val channelView = itemView as PlayWidgetCardLargeChannelView

        init {
            channelView.setListener(object : PlayWidgetCardLargeChannelView.Listener {

                override fun onChannelClicked(view: View, item: PlayWidgetChannelUiModel) {
                    listener.onChannelClicked(view, item, adapterPosition)
                }

                override fun onToggleReminderChannelClicked(
                    item: PlayWidgetChannelUiModel,
                    reminderType: PlayWidgetReminderType
                ) {
                    listener.onToggleReminderChannelClicked(item, reminderType, adapterPosition)
                }

                override fun onLabelPromoClicked(view: View, item: PlayWidgetChannelUiModel) {
                    listener.onLabelPromoChannelClicked(item, adapterPosition)
                }

                override fun onLabelPromoImpressed(view: View, item: PlayWidgetChannelUiModel) {
                    listener.onLabelPromoChannelImpressed(item, adapterPosition)
                }
            })
        }

        fun bind(data: PlayWidgetChannelUiModel) {
            itemView.addOnImpressionListener(data.impressHolder) {
                listener.onChannelImpressed(itemView, data, adapterPosition)
            }
            channelView.setModel(data)
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) = Channel(
                itemView = PlayWidgetCardLargeChannelView(parent.context),
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

            fun onLabelPromoChannelClicked(
                item: PlayWidgetChannelUiModel,
                position: Int
            )

            fun onLabelPromoChannelImpressed(
                item: PlayWidgetChannelUiModel,
                position: Int
            )
        }
    }
}