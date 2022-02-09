package com.tokopedia.tokopedianow.home.presentation.view.listener

import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics

class DynamicLegoBannerCallback(
    private val view: TokoNowView,
    private val analytics: HomeAnalytics
): DynamicLegoBannerListener {

    private val context by lazy { view.getFragmentPage().context }

    override fun onSeeAllSixImage(channelModel: ChannelModel, position: Int) {
        RouteManager.route(context,
            if (channelModel.channelHeader.applink.isNotEmpty())
                channelModel.channelHeader.applink else channelModel.channelHeader.url)
    }

    override fun onSeeAllFourImage(channelModel: ChannelModel, position: Int) {
    }

    override fun onSeeAllThreemage(channelModel: ChannelModel, position: Int) {
        RouteManager.route(context,
            if (channelModel.channelHeader.applink.isNotEmpty())
                channelModel.channelHeader.applink else channelModel.channelHeader.url)
    }

    override fun onSeeAllTwoImage(channelModel: ChannelModel, position: Int) {
    }

    override fun onClickGridSixImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        RouteManager.route(context,
            if (channelGrid.applink.isNotEmpty())
                channelGrid.applink else channelGrid.url)
    }

    override fun onClickGridFourImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
    }

    override fun onClickGridThreeImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        RouteManager.route(context,
            if (channelGrid.applink.isNotEmpty())
                channelGrid.applink else channelGrid.url)
        analytics.trackClickLego3Banner(position, channelModel, channelGrid)
    }

    override fun onClickGridTwoImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
    }

    override fun onImpressionGridSixImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onImpressionGridFourImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onImpressionGridThreeImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onImpressionGridTwoImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onChannelImpressionSixImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onChannelImpressionFourImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onChannelImpressionThreeImage(channelModel: ChannelModel, parentPosition: Int) {
        analytics.trackImpressionLego3Banner(channelModel)
    }

    override fun onChannelImpressionTwoImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun getDynamicLegoBannerData(channelModel: ChannelModel) {
    }
}