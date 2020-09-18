package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.analytics.v2.LegoBannerTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.Lego4AutoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import java.util.HashMap

/**
 * @author by yoasfs on 28/07/20
 */
class Lego4AutoBannerComponentCallback(val context: Context?, val homeCategoryListener: HomeCategoryListener): Lego4AutoBannerListener {
    override fun onSeeAllClicked(channelModel: ChannelModel, position: Int) {
        LegoBannerTracking.sendLegoBannerFourClickViewAll(channelModel, channelModel.id, homeCategoryListener.userId)
        RouteManager.route(context,
                if (channelModel.channelHeader.applink.isNotEmpty())
                    channelModel.channelHeader.applink else channelModel.channelHeader.url)
    }

    override fun onLegoItemImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {

    }

    override fun onLegoItemClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        LegoBannerTracking.sendLegoBannerFourClick(channelModel, channelGrid, position)
        RouteManager.route(context, channelGrid.applink)
    }

    override fun onChannelLegoImpressed(channelModel: ChannelModel, parentPosition: Int) {
        homeCategoryListener.putEEToIris(
                LegoBannerTracking.getLegoBannerFourImageImpression(
                        channelModel, parentPosition, true
                ) as HashMap<String, Any>
        )
    }
}