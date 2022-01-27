package com.tokopedia.play.widget.ui.adapter.viewholder.large

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.custom.PlayWidgetCardJumboBannerView
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel

/**
 * @author by astidhiyaa on 11/01/22
 */
class PlayWidgetCardLargeBannerViewHolder(
    itemView: View,
    private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    private val playWidgetBannerView: PlayWidgetCardJumboBannerView = itemView.findViewById(R.id.play_widget_banner_big)
    private val playWidgetBannerListener = object : PlayWidgetCardJumboBannerView.Listener {
        override fun onBannerClicked(view: View, item: PlayWidgetBannerUiModel) {
            listener.onBannerClicked(view, item, position)
        }
    }

    init {
        playWidgetBannerView.setListener(playWidgetBannerListener)
        playWidgetBannerView.setWidgetSizeType(isJumbo = false)
    }

    fun bind(item: PlayWidgetBannerUiModel) {
        playWidgetBannerView.setData(item)
    }

    companion object {
        @LayoutRes
        val layoutRes = R.layout.view_card_banner
    }

    interface Listener {

        fun onBannerClicked(
            view: View,
            item: PlayWidgetBannerUiModel,
            position: Int
        )
    }
}