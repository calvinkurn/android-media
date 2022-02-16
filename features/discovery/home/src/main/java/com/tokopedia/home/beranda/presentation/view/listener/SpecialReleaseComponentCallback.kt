package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.SpecialReleaseTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.SpecialReleaseComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by devarafikry on 14/02/22.
 */
class SpecialReleaseComponentCallback(
    val context: Context?,
    val homeCategoryListener: HomeCategoryListener
) : SpecialReleaseComponentListener {
    override fun onSpecialReleaseItemImpressed(
        grid: ChannelGrid,
        channelModel: ChannelModel,
        position: Int
    ) {
        SpecialReleaseTracking.sendSpecialReleaseItemImpression(
            trackingQueue = homeCategoryListener.getTrackingQueueObj(),
            channelModel = channelModel,
            channelGrid = grid,
            position = position,
            userId = homeCategoryListener.userId
        )
    }

    override fun onSpecialReleaseItemClicked(
        grid: ChannelGrid,
        channelModel: ChannelModel,
        position: Int,
        applink: String
    ) {
        SpecialReleaseTracking.sendSpecialReleaseItemClick(
            channelModel = channelModel,
            channelGrid = grid,
            position = position,
            userId = homeCategoryListener.userId
        )
        RouteManager.route(context, applink)
    }

    override fun onSpecialReleaseItemSeeAllClicked(channelModel: ChannelModel, applink: String) {
        SpecialReleaseTracking.sendSpecialReleaseSeeAllClick(channelModel)
        RouteManager.route(context, applink)
    }

    override fun onSpecialReleaseChannelImpressed(channelModel: ChannelModel, position: Int) {

    }

}