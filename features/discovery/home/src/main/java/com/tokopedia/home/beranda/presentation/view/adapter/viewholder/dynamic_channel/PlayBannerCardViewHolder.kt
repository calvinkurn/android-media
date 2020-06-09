package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCarouselCardDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselBannerDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselOverlayImageDataModel
import kotlinx.android.synthetic.main.play_banner_carousel.view.*

class PlayBannerCardViewHolder(
        val view: View,
        val listener: HomeCategoryListener
): AbstractViewHolder<PlayCarouselCardDataModel>(view), PlayBannerCarouselViewEventListener {

    companion object {
        @LayoutRes val LAYOUT = R.layout.play_banner_carousel
    }

    private var playCarouselCardDataModel: PlayCarouselCardDataModel? = null

    init {
        itemView.play_banner_carousel?.setListener(this)
    }

    override fun bind(element: PlayCarouselCardDataModel?) {
        playCarouselCardDataModel = element
        itemView.visibility = if(element == null) View.GONE else View.VISIBLE
        element?.playBannerCarouselDataModel?.let { itemView.play_banner_carousel?.setItem(it) }
    }

    override fun bind(element: PlayCarouselCardDataModel?, payloads: MutableList<Any>) {
        playCarouselCardDataModel = element
        itemView.visibility = if(element == null) View.GONE else View.VISIBLE
        element?.playBannerCarouselDataModel?.let { itemView.play_banner_carousel?.setItem(it) }
    }

    override fun onItemClick(dataModel: PlayBannerCarouselItemDataModel, position: Int) {
        RouteManager.route(itemView.context, dataModel.applink)
    }

    override fun onItemImpress(dataModel: PlayBannerCarouselItemDataModel, position: Int) {
        // tracker imprpession
    }

    override fun onReminderClick(dataModel: PlayBannerCarouselItemDataModel, position: Int) {

    }

    override fun onSeeMoreClick(dataModel: PlayBannerCarouselBannerDataModel, position: Int) {
        RouteManager.route(itemView.context, dataModel.applink)
    }

    override fun onOverlayImageBannerClick(dataModel: PlayBannerCarouselOverlayImageDataModel) {
        TODO("Not yet implemented")
    }

    override fun onRefreshView(dataModel: PlayBannerCarouselDataModel) {
        playCarouselCardDataModel?.let { listener.onPlayBannerCarouselRefresh(it, adapterPosition) }
    }

    fun onDestroy(){
        itemView.play_banner_carousel?.onDestroy()
    }
}
