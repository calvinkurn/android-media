package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.home.analytics.v2.CampaignWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.CampaignWidgetComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.TrackApp

/**
 * Created by yfsx on 12/10/21.
 */
class CampaignWidgetComponentCallback(
    val context: Context?,
    val homeCategoryListener: HomeCategoryListener
) : CampaignWidgetComponentListener {
    override fun onCampaignWidgetImpressed(channel: ChannelModel, parentPos: Int) {
    }

    override fun onSeeAllBannerClicked(channel: ChannelModel, applink: String) {
        homeCategoryListener.onDynamicChannelClicked(applink = applink)
        TrackApp.getInstance().gtm.sendGeneralEvent(
            CampaignWidgetTracking.getSeeAllClickTracking(
                channel
            ) as HashMap<String, Any>
        )
    }

    override fun onProductCardImpressed(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        adapterPosition: Int,
        position: Int
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            CampaignWidgetTracking.getCampaignWidgetItemImpressionTracking(
                channelGrid, channel, position, adapterPosition, homeCategoryListener.userId
            )
        )
    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        adapterPosition: Int,
        position: Int,
        applink: String
    ) {
        homeCategoryListener.onDynamicChannelClicked(applink = applink)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            CampaignWidgetTracking.getCampaignWidgetItemClickTracking(
                channelGrid, channel, position, adapterPosition, homeCategoryListener.userId
            )
        )
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        homeCategoryListener.onDynamicChannelClicked(applink = applink)
        TrackApp.getInstance().gtm.sendGeneralEvent(
            CampaignWidgetTracking.getSeeAllCardClickTracking(
                channel
            ) as HashMap<String, Any>
        )
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        homeCategoryListener.onDynamicChannelClicked(applink = applink)
    }
}