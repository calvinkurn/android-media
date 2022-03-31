package com.tokopedia.officialstore.official.presentation.listener

import com.tokopedia.applink.RouteManager
import com.tokopedia.home_component.listener.SpecialReleaseComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
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
//        SpecialReleaseTracking.sendSpecialReleaseItemImpression(
//            trackingQueue = homeCategoryListener.getTrackingQueueObj(),
//            channelModel = channelModel,
//            channelGrid = grid,
//            position = position,
//            userId = homeCategoryListener.userId
//        )
    }

    override fun onSpecialReleaseItemClicked(
        grid: ChannelGrid,
        channelModel: ChannelModel,
        position: Int,
        applink: String
    ) {
//        SpecialReleaseTracking.sendSpecialReleaseItemClick(
//            channelModel = channelModel,
//            channelGrid = grid,
//            position = position,
//            userId = homeCategoryListener.userId
//        )
//        RouteManager.route(context, applink)
    }

    override fun onSpecialReleaseItemSeeAllClicked(channelModel: ChannelModel, applink: String) {
//        SpecialReleaseTracking.sendSpecialReleaseSeeAllClick(channelModel)
//        RouteManager.route(context, applink)
    }

    override fun onSpecialReleaseItemSeeAllCardClicked(channelModel: ChannelModel, applink: String) {
//        SpecialReleaseTracking.sendSpecialReleaseSeeAllCardClick(channelModel)
//        RouteManager.route(context, applink)
    }

    override fun onSpecialReleaseChannelImpressed(channelModel: ChannelModel, position: Int) {
        //no-op
    }

}