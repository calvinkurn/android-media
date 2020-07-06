package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselBannerDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselOverlayImageDataModel
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapter
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
        private const val IS_AUTO_PLAY_SUCCESS = "success"
        private const val IS_AUTO_PLAY_FAILED = "failed"
    }

    private var playCarouselCardDataModel: ShopHomePlayCarouselUiModel? = null

    init {
        itemView.play_banner_carousel?.setListener(this)
    }

    override fun bind(element: ShopHomePlayCarouselUiModel?) {
        playCarouselCardDataModel = element
        itemView.play_banner_carousel.visibility = if(element == null) View.GONE else View.VISIBLE
        element?.playBannerCarouselDataModel?.let { itemView.play_banner_carousel?.setItem(it) }
    }

    override fun bind(element: ShopHomePlayCarouselUiModel?, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()){
            if(payloads.contains(ShopHomeAdapter.ON_DESTROY)){
                itemView.play_banner_carousel?.onDestroy()
            } else if(payloads.contains(ShopHomeAdapter.ON_RESUME)){
                itemView.play_banner_carousel?.onResume()
            } else if(payloads.contains(ShopHomeAdapter.ON_PAUSE)){
                itemView.play_banner_carousel?.onPause()
            }
        }
    }

    override fun onItemClick(dataModel: PlayBannerCarouselItemDataModel, position: Int) {
        listener.onPlayBannerClicked(
                dataModel,
                if(playCarouselCardDataModel?.playBannerCarouselDataModel?.isAutoPlay == true) IS_AUTO_PLAY_SUCCESS else IS_AUTO_PLAY_FAILED,
                playCarouselCardDataModel?.widgetId ?: "",
                position
        )
    }

    override fun onItemImpress(dataModel: PlayBannerCarouselItemDataModel, position: Int) {
        listener.onPlayBannerImpressed(
                dataModel,
                if(playCarouselCardDataModel?.playBannerCarouselDataModel?.isAutoPlay == true) IS_AUTO_PLAY_SUCCESS else IS_AUTO_PLAY_FAILED,
                playCarouselCardDataModel?.widgetId ?: "",
                position
        )
    }

    override fun onReminderClick(dataModel: PlayBannerCarouselItemDataModel, position: Int) {
        listener.onReminderClick(dataModel, position)
    }

    override fun onSeeMoreClick(dataModel: PlayBannerCarouselDataModel) {
        listener.onPlayBannerSeeMoreClick(dataModel.seeMoreApplink)
    }

    override fun onSeeMoreBannerClick(dataModel: PlayBannerCarouselBannerDataModel, position: Int) {
        listener.onPlayBannerSeeMoreClick(dataModel.applink)
    }

    override fun onOverlayImageBannerClick(dataModel: PlayBannerCarouselOverlayImageDataModel) {
        listener.onPlayLeftBannerClicked(dataModel, playCarouselCardDataModel?.widgetId ?: "")
    }

    override fun onOverlayImageBannerImpress(dataModel: PlayBannerCarouselOverlayImageDataModel) {
        listener.onPlayLeftBannerImpressed(dataModel, playCarouselCardDataModel?.widgetId ?: "")
    }

    override fun onRefreshView(dataModel: PlayBannerCarouselDataModel) {
        playCarouselCardDataModel?.let { listener.onPlayBannerCarouselRefresh(it, adapterPosition) }
    }

    fun onDestroy(){
        itemView.play_banner_carousel?.onDestroy()
    }
}
