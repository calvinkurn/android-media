package com.tokopedia.campaignlist.common.analytics

import com.tokopedia.campaignlist.common.analytics.constant.CampaignListTrackerConstant
import com.tokopedia.campaignlist.common.analytics.constant.CampaignListTrackerConstant.Label.EMPTY
import com.tokopedia.campaignlist.common.di.CampaignListScope
import com.tokopedia.track.TrackApp
import javax.inject.Inject

@CampaignListScope
class CampaignListTracker @Inject constructor() {

    companion object {
        private const val CAMPAIGN_STATUS_AVAILABLE = 5
        private const val CAMPAIGN_STATUS_UPCOMING = 6
        private const val CAMPAIGN_STATUS_UPCOMING_IN_NEAR_TIME = 14
        private const val CAMPAIGN_STATUS_ONGOING = 7
    }

    fun sendCampaignImpressionEvent(campaignId: String, shopId: String) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.IMPRESSION_CAMPAIGN_LIST,
            action = CampaignListTrackerConstant.Action.IMPRESSION_CAMPAIGN_LIST,
            category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
            label = campaignId,
            shopId = shopId,
            businessUnit = CampaignListTrackerConstant.Values.PHYSICAL_GOODS,
            currentSite = CampaignListTrackerConstant.Values.TOKOPEDIA_SELLER
        )
    }

    fun sendSelectCampaignTypeFilterClickEvent(selectedCampaignType: String, shopId: String) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_FILTER_TYPE,
            category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
            label = selectedCampaignType,
            shopId = shopId,
            businessUnit = CampaignListTrackerConstant.Values.PHYSICAL_GOODS,
            currentSite = CampaignListTrackerConstant.Values.TOKOPEDIA_SELLER
        )
    }

    fun sendOpenCampaignStatusFilterClickEvent(shopId: String) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_STATUS,
            category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
            label = EMPTY,
            shopId = shopId,
            businessUnit = CampaignListTrackerConstant.Values.PHYSICAL_GOODS,
            currentSite = CampaignListTrackerConstant.Values.TOKOPEDIA_SELLER
        )
    }

    fun sendOpenCampaignTypeFilterClickEvent(shopId: String) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_TYPE,
            category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
            label = EMPTY,
            shopId = shopId,
            businessUnit = CampaignListTrackerConstant.Values.PHYSICAL_GOODS,
            currentSite = CampaignListTrackerConstant.Values.TOKOPEDIA_SELLER
        )
    }

    fun sendSelectCampaignStatusClickEvent(selectedCampaignStatus: List<Int>, shopId: String) {
        selectedCampaignStatus.forEach { status ->
            val action = when (status) {
                CAMPAIGN_STATUS_AVAILABLE -> CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_STATUS_AVAILABLE
                CAMPAIGN_STATUS_UPCOMING, CAMPAIGN_STATUS_UPCOMING_IN_NEAR_TIME -> CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_STATUS_UPCOMING
                CAMPAIGN_STATUS_ONGOING -> CampaignListTrackerConstant.Action.CLICK_CAMPAIGN_STATUS_ONGOING
                else -> CampaignListTrackerConstant.Action.NOT_SPECIFIED
            }

            sendGeneralTracking(
                event = CampaignListTrackerConstant.Event.CLICK_RELEASE_PAGE,
                action = action,
                category = CampaignListTrackerConstant.EventCategory.SPECIAL_RELEASE,
                label = EMPTY,
                shopId = shopId,
                businessUnit = CampaignListTrackerConstant.Values.PHYSICAL_GOODS,
                currentSite = CampaignListTrackerConstant.Values.TOKOPEDIA_SELLER
            )
        }
    }

    fun sendShareButtonClickEvent(
        campaignTypeId: Int,
        campaignId: String,
        shopId: String,
        userId: String,
    ) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_SHOP_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_SHARE_BUTTON,
            category = CampaignListTrackerConstant.EventCategory.SHOP_PAGE_NPL,
            label = "$campaignTypeId - $campaignId",
            businessUnit = CampaignListTrackerConstant.Values.SHARING_EXPERIENCE,
            currentSite = CampaignListTrackerConstant.Values.TOKOPEDIA_SELLER,
            userId = userId,
            shopId = shopId
        )
    }

    fun sendShareBottomSheetDismissClickEvent(
        campaignTypeId: Int,
        campaignId: String,
        userId: String,
        shopId: String
    ) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_SHOP_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_CLOSE_SHARE_BOTTOM_SHEET,
            category = CampaignListTrackerConstant.EventCategory.SHOP_PAGE_NPL,
            label = "$campaignTypeId - $campaignId",
            businessUnit = CampaignListTrackerConstant.Values.SHARING_EXPERIENCE,
            currentSite = CampaignListTrackerConstant.Values.TOKOPEDIA_SELLER,
            userId = userId,
            shopId = shopId
        )
    }

    fun sendSelectShareChannelClickEvent(
        selectedChannel: String,
        campaignTypeId: Int,
        campaignId: String,
        userId: String,
        shopId: String
    ) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_SHOP_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_SHARING_CHANNEL,
            category = CampaignListTrackerConstant.EventCategory.SHOP_PAGE_NPL,
            label = "$selectedChannel - $campaignTypeId - $campaignId",
            businessUnit = CampaignListTrackerConstant.Values.SHARING_EXPERIENCE,
            currentSite = CampaignListTrackerConstant.Values.TOKOPEDIA_SELLER,
            userId = userId,
            shopId = shopId
        )
    }


    fun sendShareBottomSheetDisplayedEvent(
        campaignTypeId: Int,
        campaignId: String,
        userId: String,
        shopId: String
    ) {
        sendGeneralTracking(
            event = CampaignListTrackerConstant.Event.CLICK_VIEW_SHOP_PAGE,
            action = CampaignListTrackerConstant.Action.CLICK_VIEW_SHARING_CHANNEL,
            category = CampaignListTrackerConstant.EventCategory.SHOP_PAGE_NPL,
            label = "$campaignTypeId - $campaignId",
            businessUnit = CampaignListTrackerConstant.Values.SHARING_EXPERIENCE,
            currentSite = CampaignListTrackerConstant.Values.TOKOPEDIA_SELLER,
            userId = userId,
            shopId = shopId
        )
    }

    private fun sendGeneralTracking(
        event: String,
        category: String,
        action: String,
        label: String,
        shopId: String,
        businessUnit: String,
        currentSite: String,
        userId: String = EMPTY
    ) {
        val payload = mutableMapOf<String, Any>(
            CampaignListTrackerConstant.Key.EVENT to event,
            CampaignListTrackerConstant.Key.EVENT_ACTION to action,
            CampaignListTrackerConstant.Key.EVENT_CATEGORY to category,
            CampaignListTrackerConstant.Key.EVENT_LABEL to label,
            CampaignListTrackerConstant.Key.BUSINESS_UNIT to businessUnit,
            CampaignListTrackerConstant.Key.CURRENT_SITE to currentSite,
            CampaignListTrackerConstant.Key.SHOP_ID to shopId
        )

        if (userId.isNotEmpty()) {
            payload[CampaignListTrackerConstant.Key.USER_ID] = userId
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(payload)
    }

}