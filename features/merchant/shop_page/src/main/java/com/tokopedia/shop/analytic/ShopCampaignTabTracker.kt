package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BUSINESS_UNIT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PG
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CURRENT_SITE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_ACTION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_LABEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.Event.VIEW_PG_IRIS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_REMIND_ME_MAIN_BANNER_CAMPAIGN_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_TITLE_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_MAIN_BANNER_CAMPAIGN_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_TITLE_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PHYSICAL_GOODS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_BUYER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TRACKER_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_REMIND_ME_MAIN_BANNER_CAMPAIGN_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_TITLE_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_MAIN_BANNER_CAMPAIGN_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_TITLE_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.USER_ID
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel
import com.tokopedia.track.TrackApp
import javax.inject.Inject

/*
Data Layer Docs:

Campaign tab shop page widget
https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3199

Exclusive launch campaign tab revamp
https://mynakama.tokopedia.com/datatracker/requestdetail/view/4008

 */

class ShopCampaignTabTracker @Inject constructor() {
    fun sendImpressionShopBannerTimerCampaignTracker(
        uiModel: ShopWidgetDisplayBannerTimerUiModel,
        shopId: String,
        userId: String
    ) {
        val eventLabelValue = ShopUtil.joinDash(
            uiModel.getCampaignId(),
            uiModel.widgetId
        )
        val eventMap = mapOf(
            EVENT to VIEW_PG_IRIS,
            EVENT_ACTION to IMPRESSION_MAIN_BANNER_CAMPAIGN_TAB,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabelValue,
            TRACKER_ID to TRACKER_ID_IMPRESSION_MAIN_BANNER_CAMPAIGN_TAB,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to shopId,
            USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun sendClickRemindMeShopCampaignBannerTimerTracker(
        uiModel: ShopWidgetDisplayBannerTimerUiModel,
        shopId: String,
        userId: String
    ) {
        val eventLabelValue = ShopUtil.joinDash(
            uiModel.getCampaignId(),
            uiModel.widgetId
        )
        val eventMap = mapOf(
            EVENT to CLICK_PG,
            EVENT_ACTION to CLICK_REMIND_ME_MAIN_BANNER_CAMPAIGN_TAB,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabelValue,
            TRACKER_ID to TRACKER_ID_CLICK_REMIND_ME_MAIN_BANNER_CAMPAIGN_TAB,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to shopId,
            USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun impressionWidgetHeaderTitle(widgetId: String, shopId: String, userId: String) {
        val eventMap = mapOf(
            EVENT to VIEW_PG_IRIS,
            EVENT_ACTION to IMPRESSION_TITLE_WIDGET,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to widgetId,
            TRACKER_ID to TRACKER_ID_IMPRESSION_TITLE_WIDGET,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to shopId,
            USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickCtaHeaderTitle(widgetId: String, shopId: String, userId: String) {
        val eventMap = mapOf(
            EVENT to CLICK_PG,
            EVENT_ACTION to CLICK_TITLE_WIDGET,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to widgetId,
            TRACKER_ID to TRACKER_ID_CLICK_TITLE_WIDGET,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to shopId,
            USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

}
