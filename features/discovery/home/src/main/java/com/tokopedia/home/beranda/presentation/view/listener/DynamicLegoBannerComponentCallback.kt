package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.LegoBannerTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import java.util.HashMap

class DynamicLegoBannerComponentCallback(val context: Context?, val homeCategoryListener: HomeCategoryListener, val userId: String): DynamicLegoBannerListener {
    override fun onSeeAllSixImage(channelModel: ChannelModel, position: Int) {
        LegoBannerTracking.sendLegoBannerSixClickViewAll(channelModel, channelModel.id, homeCategoryListener.userId)
        RouteManager.route(context,
                if (channelModel.channelHeader.applink.isNotEmpty())
                    channelModel.channelHeader.applink else channelModel.channelHeader.url)
    }

    override fun onSeeAllFourImage(channelModel: ChannelModel, position: Int) {
        LegoBannerTracking.sendLegoBannerFourClickViewAll(channelModel, channelModel.id, homeCategoryListener.userId)
        RouteManager.route(context,
                if (channelModel.channelHeader.applink.isNotEmpty())
                    channelModel.channelHeader.applink else channelModel.channelHeader.url)
    }

    override fun onSeeAllThreemage(channelModel: ChannelModel, position: Int) {
        LegoBannerTracking.sendLegoBannerThreeClickViewAll(channelModel, channelModel.id, homeCategoryListener.userId)
        RouteManager.route(context,
                if (channelModel.channelHeader.applink.isNotEmpty())
                    channelModel.channelHeader.applink else channelModel.channelHeader.url)
    }

    override fun onClickGridSixImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        LegoBannerTracking.sendLegoBannerSixClick(channelModel, channelGrid, position, userId)
        RouteManager.route(context,
                if (channelGrid.applink.isNotEmpty())
                    channelGrid.applink else channelGrid.url)
    }

    override fun onClickGridFourImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        LegoBannerTracking.sendLegoBannerFourClick(channelModel, channelGrid, position, userId)
        RouteManager.route(context,
                if (channelGrid.applink.isNotEmpty())
                    channelGrid.applink else channelGrid.url)
    }

    override fun onClickGridThreeImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        LegoBannerTracking.sendLegoBannerThreeClick(channelModel, channelGrid, position, userId)
        RouteManager.route(context,
                if (channelGrid.applink.isNotEmpty())
                    channelGrid.applink else channelGrid.url)
    }

    override fun onChannelImpressionSixImage(channelModel: ChannelModel, parentPosition: Int) {
        homeCategoryListener.putEEToIris(
                LegoBannerTracking.getLegoBannerSixImageImpression(
                        channelModel, parentPosition, isToIris = true, userId
                ) as HashMap<String, Any>
        )
    }

    override fun onChannelImpressionFourImage(channelModel: ChannelModel, parentPosition: Int) {
        homeCategoryListener.putEEToIris(
                LegoBannerTracking.getLegoBannerFourImageImpression(
                        channelModel, parentPosition, true, userId
                ) as HashMap<String, Any>
        )
    }

    override fun onChannelImpressionThreeImage(channelModel: ChannelModel, parentPosition: Int) {
        homeCategoryListener.putEEToIris(
                LegoBannerTracking.getLegoBannerThreeImageImpression(
                        channelModel, parentPosition, true, userId
                ) as HashMap<String, Any>
        )
    }

    override fun onSeeAllTwoImage(channelModel: ChannelModel, position: Int) {
        LegoBannerTracking.sendLegoBannerTwoClickViewAll(channelModel)
        RouteManager.route(context, channelModel.channelHeader.applink)
    }

    override fun onClickGridTwoImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        LegoBannerTracking.sendLegoBannerTwoClick(channelModel, channelGrid, position, homeCategoryListener.userId)
        RouteManager.route(context,
                if (channelGrid.applink.isNotEmpty())
                    channelGrid.applink else channelGrid.url)
    }

    override fun onChannelImpressionTwoImage(channelModel: ChannelModel, parentPosition: Int) {
        homeCategoryListener.putEEToIris(
                LegoBannerTracking.getLegoBannerTwoImageImpression(
                        channelModel, parentPosition, true, homeCategoryListener.userId
                ) as HashMap<String, Any>
        )
    }

    override fun onViewportImpression(channelModel: ChannelModel) {
        homeCategoryListener.putEEToTrackingQueue(
            LegoBannerTracking.getLegoImpression(channelModel, userId)
        )
    }

    override fun getDynamicLegoBannerData(channelModel: ChannelModel) {

    }
}
