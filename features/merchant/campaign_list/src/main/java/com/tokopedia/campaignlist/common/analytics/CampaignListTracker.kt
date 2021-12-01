package com.tokopedia.campaignlist.common.analytics

import com.tokopedia.campaignlist.common.analytics.constant.CampaignListTrackerConstant
import com.tokopedia.campaignlist.common.di.CampaignListScope
import com.tokopedia.track.TrackApp
import javax.inject.Inject

@CampaignListScope
class CampaignListTracker @Inject constructor() {

    fun sendCampaignImpressionEvent(campaignId : String, shopId: String) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.IMPRESSION_CAMPAIGN_LIST,
            action = CampaignListTrackerConstant.Action.IMPRESSION_CAMPAIGN_LIST,
            category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
            label = campaignId,
            shopId = shopId
        )
    }

    fun sendCampaignTypeFilterClicked(campaignType : String, shopId: String) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_FILTER_TYPE,
            category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
            label = campaignType,
            shopId = shopId
        )
    }

    fun sendCampaignStatusFilterClicked(shopId: String) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_STATUS,
            category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
            label = "",
            shopId = shopId
        )
    }

    fun sendActiveCampaignStatusFilterClicked(shopId: String) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_STATUS_AVAILABLE,
            category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
            label = "",
            shopId = shopId
        )
    }

    fun sendUpcomingCampaignStatusFilterClicked(shopId: String) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_STATUS_UPCOMING,
            category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
            label = "",
            shopId = shopId
        )
    }

    fun sendOngoingCampaignStatusFilterClicked(shopId: String) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_STATUS_ONGOING,
            category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
            label = "",
            shopId = shopId
        )
    }

    private fun sendGeneralTracking(
        event: String,
        category: String,
        action: String,
        label: String,
        shopId: String
    ) {
        val payload = mutableMapOf<String, Any>(
            CampaignListTrackerConstant.Key.EVENT to event,
            CampaignListTrackerConstant.Key.EVENT_ACTION to action,
            CampaignListTrackerConstant.Key.EVENT_CATEGORY to category,
            CampaignListTrackerConstant.Key.EVENT_LABEL to label,
            CampaignListTrackerConstant.Key.BUSINESS_UNIT to CampaignListTrackerConstant.Values.PHYSICAL_GOODS,
            CampaignListTrackerConstant.Key.CURRENT_SITE to CampaignListTrackerConstant.Values.TOKOPEDIA_SELLER,
            CampaignListTrackerConstant.Key.SHOP_ID to shopId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(payload)
    }

}