package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

class RechargeHomepageDynamicLegoBannerCallback(val listener: OnItemBindListener):
        DynamicLegoBannerListener,
        HomeComponentListener {
    override fun onSeeAllSixImage(channelModel: ChannelModel, position: Int) {
        // Do nothing
    }

    override fun onSeeAllFourImage(channelModel: ChannelModel, position: Int) {
        // Do nothing
    }

    override fun onSeeAllThreemage(channelModel: ChannelModel, position: Int) {
        // Do nothing
    }

    override fun onClickGridSixImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        listener.onRechargeLegoBannerItemClicked(channelModel.id.toIntOrNull() ?: -1, channelGrid.id.toIntOrNull() ?: -1, position)
    }

    override fun onClickGridFourImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        // Do nothing
    }

    override fun onClickGridThreeImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        // Do nothing
    }

    override fun onImpressionGridFourImage(channelModel: ChannelModel, parentPosition: Int) {
        // Do nothing
    }

    override fun onImpressionGridSixImage(channelModel: ChannelModel, parentPosition: Int) {
        // Do nothing
    }

    override fun onImpressionGridThreeImage(channelModel: ChannelModel, parentPosition: Int) {
        // Do nothing
    }

    override fun onChannelImpressionSixImage(channelModel: ChannelModel, parentPosition: Int) {
        listener.onRechargeLegoBannerImpression(channelModel.id.toIntOrNull() ?: -1)
    }

    override fun onChannelImpressionFourImage(channelModel: ChannelModel, parentPosition: Int) {
        // Do nothing
    }

    override fun onChannelImpressionThreeImage(channelModel: ChannelModel, parentPosition: Int) {
        // Do nothing
    }

    override fun onChannelExpired(channelModel: ChannelModel, channelPosition: Int, visitable: Visitable<*>) {
        // Do nothing
    }

    override fun getDynamicLegoBanner(channelModel: ChannelModel) {
        listener.loadRechargeSectionData(channelModel.id.toIntOrNull() ?: -1)
    }
}