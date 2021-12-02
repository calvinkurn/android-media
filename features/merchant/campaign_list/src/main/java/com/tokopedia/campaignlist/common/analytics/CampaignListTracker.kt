package com.tokopedia.campaignlist.common.analytics

import com.tokopedia.campaignlist.common.analytics.constant.CampaignListTrackerConstant
import com.tokopedia.campaignlist.common.di.CampaignListScope
import com.tokopedia.track.TrackApp
import javax.inject.Inject

@CampaignListScope
class CampaignListTracker @Inject constructor() {

    companion object {
        private const val CAMPAIGN_STATUS_AVAILABLE = 5
        private const val CAMPAIGN_STATUS_UPCOMING = 6
        private const val CAMPAIGN_STATUS_ONGOING = 7
    }

    fun sendCampaignImpressionEvent(campaignId: String, shopId: String) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.IMPRESSION_CAMPAIGN_LIST,
            action = CampaignListTrackerConstant.Action.IMPRESSION_CAMPAIGN_LIST,
            category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
            label = campaignId,
            shopId = shopId
        )
    }

    fun sendSelectCampaignTypeFilterClickEvent(selectedCampaignType: String, shopId: String) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_FILTER_TYPE,
            category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
            label = selectedCampaignType,
            shopId = shopId
        )
    }

    fun sendOpenCampaignStatusFilterClickEvent(shopId: String) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_STATUS,
            category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
            label = CampaignListTrackerConstant.Label.EMPTY,
            shopId = shopId
        )
    }

    fun sendSelectCampaignStatusClickEvent(selectedCampaignStatus: Int, shopId: String) {
        val action = when (selectedCampaignStatus) {
            CAMPAIGN_STATUS_AVAILABLE -> CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_STATUS_AVAILABLE
            CAMPAIGN_STATUS_UPCOMING -> CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_STATUS_UPCOMING
            CAMPAIGN_STATUS_ONGOING -> CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_STATUS_ONGOING
            else -> CampaignListTrackerConstant.Action.NOT_SPECIFIED
        }

        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
            action = action,
            category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
            label = CampaignListTrackerConstant.Label.EMPTY,
            shopId = shopId
        )
    }

  /*  fun sendShareButtonClickEvent(selectedCampaignStatus: Int, shopId: String) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_SHOP_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_SHARE_BUTTON,
            category = CampaignListTrackerConstant.EventCategory.SHOP_PAGE_NPL,
            label = "",
            shopId = shopId
        )
    }

    fun sendShareCampaignDismissClickEvent(selectedCampaignStatus: Int, shopId: String) {


        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
            action = action,
            category = CampaignListTrackerConstant.EventCategory.SHOP_PAGE_NPL,
            label = CampaignListTrackerConstant.Label.EMPTY,
            shopId = shopId
        )
    }


    fun sendSelectShareChannelClickEvent(selectedChannel: Int, shopId: String) {


        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
            action = action,
            category = CampaignListTrackerConstant.EventCategory.SHOP_PAGE_NPL,
            label = CampaignListTrackerConstant.Label.EMPTY,
            shopId = shopId
        )
    }

    fun sendShareBottomSheetDisplayedEvent(selectedChannel: Int, shopId: String) {


        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
            action = action,
            category = CampaignListTrackerConstant.EventCategory.SHOP_PAGE_NPL,
            label = CampaignListTrackerConstant.Label.EMPTY,
            shopId = shopId
        )
    }*/

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