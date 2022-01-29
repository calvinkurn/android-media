package com.tokopedia.play.widget.sample.adapter.feed.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.widget.large.PlayWidgetCardLargeBannerView

/**
 * @author by astidhiyaa on 11/01/22
 */
class PlayWidgetCardLargeBannerViewHolder(
    itemView: View,
    private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    private val widgetCardLargeBannerView: PlayWidgetCardLargeBannerView = itemView as PlayWidgetCardLargeBannerView
    private val widgetBannerLargeListener = object : PlayWidgetCardLargeBannerView.Listener {
        override fun onBannerClicked(view: View, item: PlayWidgetBannerUiModel) {
            listener.onBannerClicked(view, item, adapterPosition)
        }
    }

    init {
        widgetCardLargeBannerView.setListener(widgetBannerLargeListener)
    }

    fun bind(item: PlayWidgetBannerUiModel) {
        itemView.addOnImpressionListener(item.impressHolder) {
            listener.onBannerImpressed(itemView, item, adapterPosition)
        }
        widgetCardLargeBannerView.setData(item)
    }

    companion object {
        @LayoutRes
        val layoutRes = R.layout.item_play_widget_card_large_banner
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