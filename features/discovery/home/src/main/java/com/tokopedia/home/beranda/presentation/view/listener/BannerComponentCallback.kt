package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.home.analytics.v2.BannerCarouselTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

class BannerComponentCallback (val context: Context?,
                               val homeCategoryListener: HomeCategoryListener): BannerComponentListener {
    override fun onBannerClickListener(position: Int, channelGrid: ChannelGrid, channelModel: ChannelModel) {
        BannerCarouselTracking.sendBannerCarouselClick(channelModel, channelGrid, position)
    }

    override fun isMainViewVisible(): Boolean {
        return homeCategoryListener.isMainViewVisible
    }

    override fun onPromoScrolled(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        homeCategoryListener.putEEToTrackingQueue(
                BannerCarouselTracking.getBannerCarouselItemImpression(channelModel, channelGrid, position)
        )
    }

    override fun onPageDragStateChanged(isDrag: Boolean) {
        homeCategoryListener.onPageDragStateChanged(isDrag)
    }

    override fun onPromoAllClick(applink: String) {

    }

    override fun onChannelBannerImpressed(channelModel: ChannelModel, parentPosition: Int) {
        homeCategoryListener.putEEToIris(
                BannerCarouselTracking.getBannerCarouselImpression(
                        channelModel, parentPosition, true
                )
        )
    }
}