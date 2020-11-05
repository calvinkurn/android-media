package com.tokopedia.play.widget.ui.adapter.viewholder.small

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallBannerUiModel
import com.tokopedia.play_common.view.loadImage

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetCardSmallBannerViewHolder(
        itemView: View,
        private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    private val ivBanner = itemView.findViewById<ImageView>(R.id.iv_banner)

    fun bind(item: PlayWidgetSmallBannerUiModel) {
        ivBanner.loadImage(item.imageUrl)
        itemView.setOnClickListener {
            listener.onBannerClicked(itemView)
            RouteManager.route(it.context, item.appLink)
        }
    }

    companion object {
        val layout = R.layout.item_play_widget_card_banner_small
    }

    interface Listener {

        fun onBannerClicked(view: View)
    }
}