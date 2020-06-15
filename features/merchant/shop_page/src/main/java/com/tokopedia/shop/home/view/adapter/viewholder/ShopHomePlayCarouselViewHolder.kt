package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselBannerDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselOverlayImageDataModel
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopPageHomePlayCarouselListener
import com.tokopedia.shop.home.view.model.ShopHomePlayCarouselUiModel
import kotlinx.android.synthetic.main.item_shop_home_play_carousel.view.*

class ShopHomePlayCarouselViewHolder(
        itemView: View,
        val listener: ShopPageHomePlayCarouselListener
) : AbstractViewHolder<ShopHomePlayCarouselUiModel>(itemView), PlayBannerCarouselViewEventListener {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_play_carousel
    }

    private var playCarouselCardDataModel: ShopHomePlayCarouselUiModel? = null

    init {
        itemView.play_banner_carousel?.setListener(this)
    }

    override fun bind(element: ShopHomePlayCarouselUiModel?) {
        playCarouselCardDataModel = element
        itemView.visibility = if(element == null) View.GONE else View.VISIBLE
        element?.playBannerCarouselDataModel?.let { itemView.play_banner_carousel?.setItem(it) }
    }

    override fun bind(element: ShopHomePlayCarouselUiModel?, payloads: MutableList<Any>) {
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
        listener.onReminderClick(dataModel, position)
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
