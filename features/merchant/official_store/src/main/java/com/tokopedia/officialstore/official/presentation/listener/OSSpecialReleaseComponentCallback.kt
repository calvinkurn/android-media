package com.tokopedia.officialstore.official.presentation.listener

import com.tokopedia.home_component.listener.SpecialReleaseComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.officialstore.analytics.OSSpecialReleaseTracking
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelEventHandler

/**
 * Created by devarafikry on 14/02/22.
 */
class OSSpecialReleaseComponentCallback(
    val dcEventHandler: DynamicChannelEventHandler,
    val userId: String
) : SpecialReleaseComponentListener {
    override fun onSpecialReleaseItemImpressed(
        grid: ChannelGrid,
        channelModel: ChannelModel,
        position: Int
    ) {
        OSSpecialReleaseTracking.sendSpecialReleaseItemImpression(
            trackingQueue = dcEventHandler.getTrackingObject()?.trackingQueueObj,
            channelModel = channelModel,
            channelGrid = grid,
            position = position,
            userId = dcEventHandler.getUserId(),
            categoryName = dcEventHandler.getOSCategory()?.title?:""
        )
    }

    override fun onSpecialReleaseItemClicked(
        grid: ChannelGrid,
        channelModel: ChannelModel,
        position: Int,
        applink: String
    ) {
        OSSpecialReleaseTracking.sendSpecialReleaseItemClick(
            channelModel = channelModel,
            channelGrid = grid,
            position = position,
            userId = dcEventHandler.getUserId(),
            categoryName = dcEventHandler.getOSCategory()?.title?:""
        )
        dcEventHandler.goToApplink(applink)
    }

    override fun onSpecialReleaseItemSeeAllClicked(channelModel: ChannelModel, applink: String) {
        OSSpecialReleaseTracking.sendSpecialReleaseSeeAllClick(channelModel, dcEventHandler.getOSCategory()?.title?:"")
        dcEventHandler.goToApplink(applink)
    }

    override fun onSpecialReleaseItemSeeAllCardClicked(channelModel: ChannelModel, applink: String) {
        OSSpecialReleaseTracking.sendSpecialReleaseSeeAllCardClick(channelModel, dcEventHandler.getOSCategory()?.title?:"")
        dcEventHandler.goToApplink(applink)
    }

    override fun onSpecialReleaseChannelImpressed(channelModel: ChannelModel, position: Int) {
        //no-op
    }

}