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
import com.tokopedia.play.widget.ui.widget.large.PlayWidgetCardLargeTranscodeView

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
        }
    }

    class Transcode private constructor(
        itemView: View,
        listener: Listener,
    ) : RecyclerView.ViewHolder(itemView) {

        private val transcodeView = itemView as PlayWidgetCardLargeTranscodeView

        init {
            transcodeView.setListener(object : PlayWidgetCardLargeTranscodeView.Listener {
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
                itemView = PlayWidgetCardLargeTranscodeView(parent.context),
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
