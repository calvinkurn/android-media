package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.LegoBannerTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.Lego6AutoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import java.util.HashMap

/**
 * @author by devarafikry on 09/02/21
 */
class Lego6AutoBannerComponentCallback(val context: Context?, val homeCategoryListener: HomeCategoryListener): Lego6AutoBannerListener {
    override fun onSeeAllClicked(channelModel: ChannelModel, position: Int) {
        LegoBannerTracking.sendLegoBannerSixAutoClickViewAll(channelModel, channelModel.id, homeCategoryListener.userId)
        RouteManager.route(context,
                if (channelModel.channelHeader.applink.isNotEmpty())
                    channelModel.channelHeader.applink else channelModel.channelHeader.url)
    }

    override fun onLegoItemImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {

    }

    override fun onTallImageClicked(channelModel: ChannelModel, parentPosition: Int) {
        LegoBannerTracking.sendLegoBannerTallSixAutoClick(channelModel, parentPosition)
        RouteManager.route(context, channelModel.channelBanner.applink)
    }

    override fun onLegoItemClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        LegoBannerTracking.sendLegoBannerSixAutoClick(channelModel, channelGrid, position)
        RouteManager.route(context, channelGrid.applink)
    }

    override fun onChannelLegoImpressed(channelModel: ChannelModel, parentPosition: Int) {
        homeCategoryListener.putEEToIris(
                LegoBannerTracking.getLegoBannerSixImageImpression(
                        channelModel, parentPosition, true
                ) as HashMap<String, Any>
        )
    }
}