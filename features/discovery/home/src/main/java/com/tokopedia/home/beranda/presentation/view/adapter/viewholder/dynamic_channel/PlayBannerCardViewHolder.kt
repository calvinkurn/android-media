package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.PlayWidgetCarouselTracking
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
        if(element?.playBannerCarouselDataModel?.channelList?.isEmpty() == true){
            playBannerCarouselView?.showRefreshShimmer()
        } else {
            playBannerCarouselView?.removeRefreshShimmer()
            element?.playBannerCarouselDataModel?.let {
                if (it.channelList.isNotEmpty()) {
                    playBannerCarouselView?.setItem(it)
                }
            }
        }
    }

    override fun bind(element: PlayCarouselCardDataModel?, payloads: MutableList<Any>) {
        playCarouselCardDataModel = element
        if(element?.playBannerCarouselDataModel?.channelList?.isEmpty() == true){
            playBannerCarouselView?.showRefreshShimmer()
        } else {
            playBannerCarouselView?.removeRefreshShimmer()
            element?.playBannerCarouselDataModel?.let {
                if(it.channelList.isNotEmpty()){
                    playBannerCarouselView?.setItem(it)
                }
            }
        }
    }

    override fun onItemClick(dataModel: PlayBannerCarouselItemDataModel, position: Int) {
        val autoPlay = if(playCarouselCardDataModel?.playBannerCarouselDataModel?.isAutoPlay == true) "success" else "false"
        listener.sendEETracking(
                PlayWidgetCarouselTracking.getClickBanner(
                        channelId = dataModel.channelId,
                        channelName = dataModel.channelTitle,
                        autoPlay = autoPlay,
                        shopId = dataModel.partnerId,
                        widgetPosition = adapterPosition.toString(),
                        creativeName = dataModel.coverUrl,
                        bannerId = playCarouselCardDataModel?.channel?.id ?: "",
                        userId = listener.userId,
                        position = playCarouselCardDataModel?.channel?.brandId ?: "1",
                        positionFold = if((playCarouselCardDataModel?.position ?: -1) <= 2) "0" else "1"
                )
        )
        listener.onPlayV2Click(dataModel)
    }

    override fun onItemImpress(dataModel: PlayBannerCarouselItemDataModel, position: Int) {
        val autoPlay = if(playCarouselCardDataModel?.playBannerCarouselDataModel?.isAutoPlay == true) "success" else "false"
        listener.putEEToTrackingQueue(PlayWidgetCarouselTracking.getImpressionBanner(
                channelId = dataModel.channelId,
                channelName = dataModel.channelTitle,
                autoPlay = autoPlay,
                shopId = dataModel.partnerId,
                widgetPosition = adapterPosition.toString(),
                creativeName = dataModel.coverUrl,
                bannerId = playCarouselCardDataModel?.channel?.id ?: "",
                userId = listener.userId,
                position = (position+1).toString(),
                positionFold = if((playCarouselCardDataModel?.position ?: -1) <= 2) "0" else "1"
        ))
    }

    override fun onReminderClick(dataModel: PlayBannerCarouselItemDataModel, position: Int) {
        listener.sendEETracking(
                if(!dataModel.remindMe) {
                    PlayWidgetCarouselTracking.getClickRemoveRemind(
                            channelId = dataModel.channelId,
                            userId = listener.userId,
                            notifierId = (position + 1).toString()
                    )
                } else {
                    PlayWidgetCarouselTracking.getClickAddRemind(
                            channelId = dataModel.channelId,
                            userId = listener.userId,
                            notifierId = (position + 1).toString()
                    )
                }
        )
        listener.onPlayBannerReminderClick(dataModel)
    }

    override fun onSeeMoreBannerClick(dataModel: PlayBannerCarouselBannerDataModel, position: Int) {
        listener.sendEETracking(PlayWidgetCarouselTracking.getClickSeeOtherContent(dataModel.imageUrl, listener.userId))
        RouteManager.route(itemView.context, dataModel.applink)
    }

    override fun onSeeMoreClick(dataModel: PlayBannerCarouselDataModel) {
        listener.sendEETracking(PlayWidgetCarouselTracking.getClickSeeAll(listener.userId))
        RouteManager.route(itemView.context, dataModel.seeMoreApplink)
    }

    override fun onOverlayImageBannerClick(dataModel: PlayBannerCarouselOverlayImageDataModel) {
        listener.sendEETracking(PlayWidgetCarouselTracking.getClickLeftBanner(
                channelId = playCarouselCardDataModel?.channel?.id ?: "",
                widgetPosition = adapterPosition.toString(),
                creativeName = dataModel.imageUrl,
                bannerId = playCarouselCardDataModel?.channel?.id ?: "",
                userId = listener.userId,
                position = adapterPosition.toString(),
                shopName = playCarouselCardDataModel?.playBannerCarouselDataModel?.title ?: "",
                promoCode = "",
                positionFold = if((playCarouselCardDataModel?.position ?: -1) <= 2) "0" else "1"
        ))
        RouteManager.route(itemView.context, dataModel.applink)
    }

    override fun onOverlayImageBannerImpress(dataModel: PlayBannerCarouselOverlayImageDataModel) {
        listener.putEEToTrackingQueue(PlayWidgetCarouselTracking.getImpressionLeftBanner(
                channelId = playCarouselCardDataModel?.channel?.id ?: "",
                widgetPosition = adapterPosition.toString(),
                creativeName = dataModel.imageUrl,
                bannerId = playCarouselCardDataModel?.channel?.id ?: "",
                userId = listener.userId,
                position = adapterPosition.toString(),
                shopName = playCarouselCardDataModel?.playBannerCarouselDataModel?.title ?: "",
                promoCode = "",
                positionFold = if((playCarouselCardDataModel?.position ?: -1) <= 2) "0" else "1"
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
