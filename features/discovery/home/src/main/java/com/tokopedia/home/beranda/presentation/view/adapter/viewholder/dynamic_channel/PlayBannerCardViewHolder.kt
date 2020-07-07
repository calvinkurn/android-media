package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCarouselCardDataModel
import com.tokopedia.play_common.widget.playBannerCarousel.PlayBannerCarousel
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
    private var playBannerCarouselView: PlayBannerCarousel? = null

    init {
        playBannerCarouselView = itemView.play_banner_carousel
        playBannerCarouselView?.setListener(this)
    }

    override fun bind(element: PlayCarouselCardDataModel?) {
        playCarouselCardDataModel = element
        element?.playBannerCarouselDataModel?.let {
            if(it.channelList.isNotEmpty()){
                playBannerCarouselView?.setItem(it)
            }
        }
    }

    override fun bind(element: PlayCarouselCardDataModel?, payloads: MutableList<Any>) {
        playCarouselCardDataModel = element
        element?.playBannerCarouselDataModel?.let {
            if(it.channelList.isNotEmpty()){
                playBannerCarouselView?.setItem(it)
            }
        }
    }

    override fun onItemClick(dataModel: PlayBannerCarouselItemDataModel, position: Int) {
        val autoPlay = if(playCarouselCardDataModel?.playBannerCarouselDataModel?.isAutoPlay == true) "success" else "false"
        listener.sendEETracking(
                HomePageTrackingV2.PlayWidgetCarousel.getClickBanner(
                        channelId = dataModel.channelId,
                        channelName = dataModel.channelTitle,
                        autoPlay = autoPlay,
                        shopId = dataModel.partnerId,
                        widgetPosition = adapterPosition.toString(),
                        creativeName = dataModel.coverUrl,
                        bannerId = playCarouselCardDataModel?.channel?.id ?: "",
                        userId = listener.userId,
                        position = playCarouselCardDataModel?.channel?.brandId ?: "1"
                )
        )
        RouteManager.route(itemView.context, dataModel.applink)
    }

    override fun onItemImpress(dataModel: PlayBannerCarouselItemDataModel, position: Int) {
        val autoPlay = if(playCarouselCardDataModel?.playBannerCarouselDataModel?.isAutoPlay == true) "success" else "false"
        listener.putEEToTrackingQueue(HomePageTrackingV2.PlayWidgetCarousel.getImpressionBanner(
                channelId = dataModel.channelId,
                channelName = dataModel.channelTitle,
                autoPlay = autoPlay,
                shopId = dataModel.partnerId,
                widgetPosition = adapterPosition.toString(),
                creativeName = dataModel.coverUrl,
                bannerId = playCarouselCardDataModel?.channel?.id ?: "",
                userId = listener.userId,
                position = playCarouselCardDataModel?.channel?.brandId ?: "1"
        ))
    }

    override fun onReminderClick(dataModel: PlayBannerCarouselItemDataModel, position: Int) {
        listener.sendEETracking(
                if(dataModel.remindMe) {
                    HomePageTrackingV2.PlayWidgetCarousel.getClickRemoveRemind(
                            channelId = dataModel.channelId,
                            userId = listener.userId,
                            notifierId = (position + 1).toString()
                    )
                } else {
                    HomePageTrackingV2.PlayWidgetCarousel.getClickAddRemind(
                            channelId = dataModel.channelId,
                            userId = listener.userId,
                            notifierId = (position + 1).toString()
                    )
                }
        )
        listener.onPlayBannerReminderClick(dataModel)
    }

    override fun onSeeMoreBannerClick(dataModel: PlayBannerCarouselBannerDataModel, position: Int) {
        listener.sendEETracking(HomePageTrackingV2.PlayWidgetCarousel.getClickSeeAll(dataModel.imageUrl, listener.userId))
        RouteManager.route(itemView.context, dataModel.applink)
    }

    override fun onSeeMoreClick(dataModel: PlayBannerCarouselDataModel) {
        listener.sendEETracking(HomePageTrackingV2.PlayWidgetCarousel.getClickSeeAll(dataModel.imageUrl, listener.userId))
        RouteManager.route(itemView.context, dataModel.seeMoreApplink)
    }

    override fun onOverlayImageBannerClick(dataModel: PlayBannerCarouselOverlayImageDataModel) {
        listener.sendEETracking(HomePageTrackingV2.PlayWidgetCarousel.getClickLeftBanner(
                channelId = playCarouselCardDataModel?.channel?.id ?: "",
                widgetPosition = adapterPosition.toString(),
                creativeName = dataModel.imageUrl,
                bannerId = playCarouselCardDataModel?.channel?.id ?: "",
                userId = listener.userId,
                position = playCarouselCardDataModel?.channel?.brandId ?: "1",
                shopName = playCarouselCardDataModel?.playBannerCarouselDataModel?.title ?: "",
                promoCode = ""
        ))
    }

    override fun onOverlayImageBannerImpress(dataModel: PlayBannerCarouselOverlayImageDataModel) {
        listener.putEEToTrackingQueue(HomePageTrackingV2.PlayWidgetCarousel.getImpressionLeftBanner(
                channelId = playCarouselCardDataModel?.channel?.id ?: "",
                widgetPosition = adapterPosition.toString(),
                creativeName = dataModel.imageUrl,
                bannerId = playCarouselCardDataModel?.channel?.id ?: "",
                userId = listener.userId,
                position = playCarouselCardDataModel?.channel?.brandId ?: "1",
                shopName = playCarouselCardDataModel?.playBannerCarouselDataModel?.title ?: "",
                promoCode = ""
        ))
    }

    override fun onRefreshView(dataModel: PlayBannerCarouselDataModel) {
        playCarouselCardDataModel?.let { listener.onPlayBannerCarouselRefresh(it, adapterPosition) }
    }

    fun onPause(){
        playBannerCarouselView?.onPause()
    }

    fun onResume(){
        playBannerCarouselView?.onResume()
    }

    fun onDestroy(){
        playBannerCarouselView?.onDestroy()
    }
}
