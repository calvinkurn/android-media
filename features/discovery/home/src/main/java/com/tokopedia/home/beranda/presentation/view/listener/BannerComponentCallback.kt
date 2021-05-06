package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.BannerCarouselTracking
import com.tokopedia.home.analytics.v2.LegoBannerTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

class BannerComponentCallback (val context: Context?,
                               val homeCategoryListener: HomeCategoryListener): BannerComponentListener {
    // tracker
    private val impressionStatusList = mutableMapOf<String, Boolean>()

    override fun onBannerClickListener(position: Int, channelGrid: ChannelGrid, channelModel: ChannelModel) {
        BannerCarouselTracking.sendBannerCarouselClick(channelModel, channelGrid, position, homeCategoryListener.userId)
        if (channelGrid.applink.isNotEmpty()) {
            homeCategoryListener.onSectionItemClicked(channelGrid.applink)
        } else {
            homeCategoryListener.onSectionItemClicked(channelGrid.url)
        }
    }

    override fun isMainViewVisible(): Boolean {
        return homeCategoryListener.isMainViewVisible
    }

    override fun onPromoScrolled(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        homeCategoryListener.putEEToTrackingQueue(
                BannerCarouselTracking.getBannerCarouselItemImpression(channelModel, channelGrid, position, false, homeCategoryListener.userId)
        )
        impressionStatusList[channelGrid.id] = true
    }

    override fun onPageDragStateChanged(isDrag: Boolean) {
        homeCategoryListener.onPageDragStateChanged(isDrag)
    }

    override fun onPromoAllClick(channelModel: ChannelModel) {
        BannerCarouselTracking.sendBannerCarouselSeeAllClick(channelModel, homeCategoryListener.userId)
        RouteManager.route(context,
                if (channelModel.channelHeader.applink.isNotEmpty())
                    channelModel.channelHeader.applink else channelModel.channelHeader.url)
    }

    override fun onChannelBannerImpressed(channelModel: ChannelModel, parentPosition: Int) {
        homeCategoryListener.putEEToIris(
                BannerCarouselTracking.getBannerCarouselImpression(
                        channelModel, parentPosition, true
                )
        )
    }

    override fun isBannerImpressed(id: String): Boolean {
        return if (impressionStatusList.containsKey(id)) {
            impressionStatusList[id]?:false
        } else false
    }

    fun resetImpression() {
        impressionStatusList.clear()
    }
}