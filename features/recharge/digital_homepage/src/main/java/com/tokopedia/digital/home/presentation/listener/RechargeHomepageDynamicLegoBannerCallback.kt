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

    // TODO: Add lego banner click listener; current default is to 6 image
    override fun onClickGridSixImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {

    }

    override fun onClickGridFourImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        // Do nothing
    }

    override fun onClickGridThreeImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        // Do nothing
    }

    // TODO: Add lego banner item impression listener; current default is to 6 image
    override fun onImpressionGridSixImage(channelModel: ChannelModel, parentPosition: Int) {

    }

    override fun onImpressionGridFourImage(channelModel: ChannelModel, parentPosition: Int) {
        // Do nothing
    }

    override fun onImpressionGridThreeImage(channelModel: ChannelModel, parentPosition: Int) {
        // Do nothing
    }

    // TODO: Add lego banner section impression listener; current default is to 6 image
    override fun onChannelImpressionSixImage(channelModel: ChannelModel, parentPosition: Int) {

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
}