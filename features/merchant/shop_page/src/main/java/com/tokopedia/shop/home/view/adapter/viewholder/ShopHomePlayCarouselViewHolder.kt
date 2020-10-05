package com.tokopedia.shop.home.view.adapter.viewholder

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.play_common.widget.playBannerCarousel.event.PlayBannerCarouselViewEventListener
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselBannerDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselOverlayImageDataModel
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.fragment.ShopPageHomeFragment
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
        const val ON_PAUSE = "on_pause"
        const val ON_RESUME = "on_resume"
        const val ON_DESTROY = "on_resume"
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
            val bundle = payloads.first() as Bundle
            when {
                bundle.containsKey(ON_DESTROY) -> {
                    itemView.play_banner_carousel?.onDestroy()
                }
                bundle.containsKey(ON_RESUME) -> {
                    itemView.play_banner_carousel?.onResume()
                }
                bundle.containsKey(ON_PAUSE) -> {
                    itemView.play_banner_carousel?.onPause()
                }
                 bundle.containsKey(ShopPageHomeFragment.UPDATE_REMIND_ME_PLAY) -> {
                    element?.playBannerCarouselDataModel?.let{ playCarouselCardDataModel ->
                        if(bundle.containsKey(ShopPageHomeFragment.UPDATE_REMIND_ME_PLAY)){
                            itemView.play_banner_carousel?.setItem(playCarouselCardDataModel.copy(
                                channelList = playCarouselCardDataModel.channelList.map {
                                    if(it.getId() == bundle.getString(ShopPageHomeFragment.UPDATE_REMIND_ME_PLAY_ID) && it is PlayBannerCarouselItemDataModel){
                                        it.copy(remindMe = !it.remindMe)
                                    } else {
                                        it
                                    }
                                }
                            ))
                        }
                    }
                }
            }
        }
    }

    override fun onItemClick(dataModel: PlayBannerCarouselItemDataModel, position: Int) {
        listener.onPlayBannerClicked(
                dataModel,
                playCarouselCardDataModel?.playBannerCarouselDataModel?.isAutoPlay.toString(),
                playCarouselCardDataModel?.widgetId ?: "",
                isFoldPosition(adapterPosition),
                position
        )
    }

    override fun onItemImpress(dataModel: PlayBannerCarouselItemDataModel, position: Int) {
        listener.onPlayBannerImpressed(
                dataModel,
                playCarouselCardDataModel?.playBannerCarouselDataModel?.isAutoPlay.toString(),
                playCarouselCardDataModel?.widgetId ?: "",
                isFoldPosition(adapterPosition),
                position
        )
    }

    override fun onReminderClick(dataModel: PlayBannerCarouselItemDataModel, position: Int) {
        listener.onReminderClick(dataModel, adapterPosition)
    }

    override fun onSeeMoreClick(dataModel: PlayBannerCarouselDataModel) {
        listener.onPlayBannerSeeMoreClick(dataModel.seeMoreApplink)
    }

    override fun onSeeMoreBannerClick(dataModel: PlayBannerCarouselBannerDataModel, position: Int) {
        listener.onPlayBannerSeeMoreClick(dataModel.applink)
    }

    override fun onOverlayImageBannerClick(dataModel: PlayBannerCarouselOverlayImageDataModel) {
        listener.onPlayLeftBannerClicked(dataModel, playCarouselCardDataModel?.widgetId ?: "", isFoldPosition(adapterPosition), 0)
    }

    override fun onOverlayImageBannerImpress(dataModel: PlayBannerCarouselOverlayImageDataModel) {
        listener.onPlayLeftBannerImpressed(
                dataModel,
                playCarouselCardDataModel?.widgetId ?: "",
                isFoldPosition(adapterPosition),
                0
        )
    }

    override fun onRefreshView(dataModel: PlayBannerCarouselDataModel) {
        playCarouselCardDataModel?.let { listener.onPlayBannerCarouselRefresh(it, adapterPosition) }
    }

    fun onResume(){
        itemView.play_banner_carousel?.onResume()
    }

    fun onPause(){
        itemView.play_banner_carousel?.onPause()
    }

    fun onDestroy(){
        itemView.play_banner_carousel?.onDestroy()
    }

    private fun isFoldPosition(position: Int) = if(position <= 2) 0 else 1
}
