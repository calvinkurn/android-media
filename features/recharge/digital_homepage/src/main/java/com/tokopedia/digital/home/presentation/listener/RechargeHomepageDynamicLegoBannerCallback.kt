package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

class RechargeHomepageDynamicLegoBannerCallback(val listener: RechargeHomepageItemListener):
        DynamicLegoBannerListener,
        HomeComponentListener {
    override fun onSeeAllSixImage(channelModel: ChannelModel, position: Int) {
        listener.onRechargeBannerAllItemClicked(RechargeHomepageSections.Section(applink = channelModel.channelHeader.applink))
    }

    override fun onSeeAllFourImage(channelModel: ChannelModel, position: Int) {
        listener.onRechargeBannerAllItemClicked(RechargeHomepageSections.Section(applink = channelModel.channelHeader.applink))
    }

    override fun onSeeAllThreemage(channelModel: ChannelModel, position: Int) {
        listener.onRechargeBannerAllItemClicked(RechargeHomepageSections.Section(applink = channelModel.channelHeader.applink))
    }

    override fun onClickGridSixImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        listener.onRechargeLegoBannerItemClicked(channelModel.id, channelGrid.id, position)
    }

    override fun onClickGridFourImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        // Do nothing
    }

    override fun onClickGridThreeImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        listener.onRechargeLegoBannerItemClicked(channelModel.id, channelGrid.id, position)
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
        listener.onRechargeLegoBannerImpression(channelModel.id)
    }

    override fun onChannelImpressionFourImage(channelModel: ChannelModel, parentPosition: Int) {
        listener.onRechargeLegoBannerImpression(channelModel.id)
    }

    override fun onChannelImpressionThreeImage(channelModel: ChannelModel, parentPosition: Int) {
        listener.onRechargeLegoBannerImpression(channelModel.id)
    }

    override fun onChannelExpired(channelModel: ChannelModel, channelPosition: Int, visitable: Visitable<*>) {
        // Do nothing
    }

    override fun getDynamicLegoBannerData(channelModel: ChannelModel) {
        listener.loadRechargeSectionData(channelModel.id, channelModel.name) //update to hit section with section name
    }
    override fun onSeeAllTwoImage(channelModel: ChannelModel, position: Int) {
        RechargeHomepageSections.Section(applink = channelModel.channelHeader.applink)
    }

    override fun onClickGridTwoImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
    }

    override fun onImpressionGridTwoImage(channelModel: ChannelModel, parentPosition: Int) {

    }

    override fun onChannelImpressionTwoImage(channelModel: ChannelModel, parentPosition: Int) {
        listener.onRechargeLegoBannerImpression(channelModel.id)
    }
}
