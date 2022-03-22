package com.tokopedia.play.widget.ui.widget.small.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.widget.small.PlayWidgetCardSmallBannerView
import com.tokopedia.play.widget.ui.widget.small.PlayWidgetCardSmallChannelView

/**
 * Created by kenny.hadisaputra on 24/01/22
 */
class PlayWidgetSmallViewHolder {

    class Banner private constructor(
        itemView: View,
        listener: Listener,
    ) : RecyclerView.ViewHolder(itemView) {

        private val bannerView = itemView as PlayWidgetCardSmallBannerView

        init {
            bannerView.setListener(object : PlayWidgetCardSmallBannerView.Listener {
                override fun onBannerClicked(view: PlayWidgetCardSmallBannerView) {
                    listener.onBannerClicked(view)
                }
            })
        }

        fun bind(data: PlayWidgetBannerUiModel) {
            bannerView.setData(data)
        }

        companion object {
            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) = Banner(
                itemView = PlayWidgetCardSmallBannerView(parent.context),
                listener = listener,
            )
        }

        interface Listener {
            fun onBannerClicked(view: View)
        }
    }

    class Channel private constructor(
        itemView: View,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(itemView) {

        private val channelView = itemView as PlayWidgetCardSmallChannelView

        init {
            channelView.setListener(object : PlayWidgetCardSmallChannelView.Listener {
                override fun onChannelClicked(
                    view: PlayWidgetCardSmallChannelView,
                    model: PlayWidgetChannelUiModel
                ) {
                    listener.onChannelClicked(view, model, adapterPosition)
                }
            })
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
                listener: Listener,
            ) = Channel(
                itemView = PlayWidgetCardSmallChannelView(parent.context),
                listener = listener,
            )
        }

        interface Listener {

            fun onChannelImpressed(
                view: View,
                item: PlayWidgetChannelUiModel,
                position: Int,
            )

            fun onChannelClicked(
                view: View,
                item: PlayWidgetChannelUiModel,
                position: Int,
            )
        }
    }
}