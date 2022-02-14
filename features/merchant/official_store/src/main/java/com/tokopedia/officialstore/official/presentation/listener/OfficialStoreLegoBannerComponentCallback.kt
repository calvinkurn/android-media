package com.tokopedia.officialstore.official.presentation.listener

import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.officialstore.analytics.OSLegoBannerTracking
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelEventHandler
import java.util.HashMap

class OfficialStoreLegoBannerComponentCallback(private val dcEventHandler: DynamicChannelEventHandler): DynamicLegoBannerListener {
    override fun onSeeAllSixImage(channelModel: ChannelModel, position: Int) {
        dcEventHandler.onClickLegoHeaderActionText(channelModel.channelHeader.applink)
    }


    override fun onSeeAllThreemage(channelModel: ChannelModel, position: Int) {
        dcEventHandler.onClickLegoHeaderActionText(channelModel.channelHeader.applink)
    }

    override fun onClickGridSixImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        dcEventHandler.onClickLegoImage(channelModel, position)
    }



    override fun onClickGridThreeImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        dcEventHandler.onClickLegoImage(channelModel, position)
    }

    override fun onImpressionGridSixImage(channelModel: ChannelModel, parentPosition: Int) {

    }


    override fun onImpressionGridThreeImage(channelModel: ChannelModel, parentPosition: Int) {

    }

    override fun onChannelImpressionSixImage(channelModel: ChannelModel, parentPosition: Int) {
        dcEventHandler.legoImpression(channelModel)
    }

    override fun onChannelImpressionThreeImage(channelModel: ChannelModel, parentPosition: Int) {
        dcEventHandler.legoImpression(channelModel)
    }

    override fun getDynamicLegoBannerData(channelModel: ChannelModel) {

    }

    //new lego

    override fun onSeeAllTwoImage(channelModel: ChannelModel, position: Int) {
        dcEventHandler.goToApplink(channelModel.channelHeader.applink)
        dcEventHandler.getTrackingObject()?.trackerObj?.sendGeneralEvent(OSLegoBannerTracking.getLegoBannerTwoViewAllClick(channelModel, dcEventHandler.getUserId()))
    }

    override fun onClickGridTwoImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        dcEventHandler.goToApplink(channelGrid.applink)
        dcEventHandler.getTrackingObject()?.trackerObj?.sendEnhanceEcommerceEvent(OSLegoBannerTracking.getLegoBannerTwoClick(channelModel, channelGrid, position, dcEventHandler.getUserId()))
    }

    override fun onImpressionGridTwoImage(channelModel: ChannelModel, parentPosition: Int) {
      }

    override fun onChannelImpressionTwoImage(channelModel: ChannelModel, parentPosition: Int) {
        dcEventHandler.getTrackingObject()?.trackingQueueObj?.putEETracking(OSLegoBannerTracking.getLegoBannerTwoImageImpression(channel = channelModel, position = parentPosition, userId = dcEventHandler.getUserId()) as HashMap<String, Any>)
    }


    override fun onClickGridFourImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int) {
        dcEventHandler.goToApplink(channelGrid.applink)
        dcEventHandler.getTrackingObject()?.trackerObj?.sendEnhanceEcommerceEvent(OSLegoBannerTracking.getLegoBannerFourClick(channelModel, channelGrid, position, dcEventHandler.getUserId()))
    }

    override fun onSeeAllFourImage(channelModel: ChannelModel, position: Int) {
        dcEventHandler.goToApplink(channelModel.channelHeader.applink)
        dcEventHandler.getTrackingObject()?.trackerObj?.sendGeneralEvent(OSLegoBannerTracking.getLegoBannerFourViewAllClick(channelModel, dcEventHandler.getUserId()))
    }

    override fun onImpressionGridFourImage(channelModel: ChannelModel, parentPosition: Int) {

    }

    override fun onChannelImpressionFourImage(channelModel: ChannelModel, parentPosition: Int) {
        dcEventHandler.getTrackingObject()?.trackingQueueObj?.putEETracking(OSLegoBannerTracking.getLegoBannerFourImageImpression(channel = channelModel, position = parentPosition, userId = dcEventHandler.getUserId()) as HashMap<String, Any>)
    }

}