package com.tokopedia.officialstore.official.presentation.listener

import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelEventHandler

class OfficialStoreLegoBannerComponentCallback(private val dcEventHandler: DynamicChannelEventHandler): DynamicLegoBannerListener {
    override fun onSeeAllSixImage(channelModel: ChannelModel, position: Int) {
        dcEventHandler.onClickLegoHeaderActionText(channelModel.channelHeader.applink)
    }

    override fun onSeeAllFourImage(channelModel: ChannelModel, position: Int) {
        dcEventHandler.onClickLegoHeaderActionText(channelModel.channelHeader.applink)
    }

    override fun onSeeAllThreemage(channelModel: ChannelModel, position: Int) {
        dcEventHandler.onClickLegoHeaderActionText(channelModel.channelHeader.applink)
    }

    override fun onClickGridSixImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        dcEventHandler.onClickLegoImage(channelModel, position)
    }

    override fun onClickGridFourImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        dcEventHandler.onClickLegoImage(channelModel, position)
    }

    override fun onClickGridThreeImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        dcEventHandler.onClickLegoImage(channelModel, position)
    }

    override fun onImpressionGridSixImage(channelModel: ChannelModel, parentPosition: Int) {

    }

    override fun onImpressionGridFourImage(channelModel: ChannelModel, parentPosition: Int) {

    }

    override fun onImpressionGridThreeImage(channelModel: ChannelModel, parentPosition: Int) {

    }

    override fun onChannelImpressionSixImage(channelModel: ChannelModel, parentPosition: Int) {
        dcEventHandler.legoImpression(channelModel)
    }

    override fun onChannelImpressionFourImage(channelModel: ChannelModel, parentPosition: Int) {
        dcEventHandler.legoImpression(channelModel)
    }

    override fun onChannelImpressionThreeImage(channelModel: ChannelModel, parentPosition: Int) {
        dcEventHandler.legoImpression(channelModel)
    }

    override fun getDynamicLegoBannerData(channelModel: ChannelModel) {

    }

    override fun onSeeAllTwoImage(channelModel: ChannelModel, position: Int) {
    }

    override fun onClickGridTwoImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
    }

    override fun onImpressionGridTwoImage(channelModel: ChannelModel, parentPosition: Int) {
    }

    override fun onChannelImpressionTwoImage(channelModel: ChannelModel, parentPosition: Int) {
    }
}