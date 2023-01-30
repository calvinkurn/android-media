package com.tokopedia.dilayanitokopedia.home.presentation.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

class DtDynamicLegoBannerCallback(
    private val context: Context
) : DynamicLegoBannerListener {

    override fun onSeeAllSixImage(channelModel: ChannelModel, position: Int) {
        RouteManager.route(
            context,
            channelModel.channelHeader.applink.ifEmpty { channelModel.channelHeader.url }
        )
    }

    override fun onSeeAllFourImage(channelModel: ChannelModel, position: Int) {
        // no-op
    }

    override fun onSeeAllThreemage(channelModel: ChannelModel, position: Int) {
        RouteManager.route(context, channelModel.channelHeader.applink.ifEmpty { channelModel.channelHeader.url })
    }

    override fun onSeeAllTwoImage(channelModel: ChannelModel, position: Int) {
        // no-op
    }

    override fun onClickGridSixImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        RouteManager.route(context, channelGrid.applink.ifEmpty { channelGrid.url })
    }

    override fun onClickGridFourImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        // no-op
    }

    override fun onClickGridThreeImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        // no-op
    }

    override fun onClickGridTwoImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        // no-op
    }

    override fun onImpressionGridSixImage(channelModel: ChannelModel, parentPosition: Int) {
        // no-op
    }

    override fun onImpressionGridFourImage(channelModel: ChannelModel, parentPosition: Int) {
        // no-op
    }

    override fun onImpressionGridThreeImage(channelModel: ChannelModel, parentPosition: Int) {
        // no-op
    }

    override fun onImpressionGridTwoImage(channelModel: ChannelModel, parentPosition: Int) {
        // no-op
    }

    override fun onChannelImpressionSixImage(channelModel: ChannelModel, parentPosition: Int) {
        // no-op
    }

    override fun onChannelImpressionFourImage(channelModel: ChannelModel, parentPosition: Int) {
        // no-op
    }

    override fun onChannelImpressionThreeImage(channelModel: ChannelModel, parentPosition: Int) {
        // no-op
    }

    override fun onChannelImpressionTwoImage(channelModel: ChannelModel, parentPosition: Int) {
        // no-op
    }

    override fun getDynamicLegoBannerData(channelModel: ChannelModel) {
        // no-op
    }
}
