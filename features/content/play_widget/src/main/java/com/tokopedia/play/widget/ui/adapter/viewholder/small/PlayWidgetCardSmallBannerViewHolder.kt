package com.tokopedia.play.widget.ui.adapter.viewholder.small

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallBannerUiModel
import com.tokopedia.play_common.view.loadImage
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 07/10/20
 */
class PlayWidgetCardSmallBannerViewHolder(
        itemView: View,
        private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    private val ivBanner: ImageView = itemView.findViewById(R.id.iv_banner)
    private val tvFallback: Typography = itemView.findViewById(R.id.tv_fallback)

    private val bannerlistener = object : ImageHandler.ImageLoaderStateListener {

        override fun successLoad() {
            ivBanner.visibility = View.VISIBLE
            tvFallback.visibility = View.GONE
        }

        override fun failedLoad() {
            ivBanner.visibility = View.GONE
            tvFallback.visibility = View.VISIBLE
        }
    }

    fun bind(item: PlayWidgetSmallBannerUiModel) {
        ivBanner.loadImage(item.imageUrl, bannerlistener)
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