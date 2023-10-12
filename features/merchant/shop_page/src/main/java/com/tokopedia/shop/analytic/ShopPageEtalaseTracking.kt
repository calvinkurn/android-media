package com.tokopedia.shop.analytic

import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

/*
Data Layer Docs:

https://mynakama.tokopedia.com/datatracker/requestdetail/view/4130
 */

class ShopPageEtalaseTracking(
    trackingQueue: TrackingQueue
) : ShopPageTracking(trackingQueue) {


    fun clickShareEtalase(
        shopId: String, etalaseId: String,
        isAffiliateShareIcon: Boolean, userId: String,
    ) {
        val shareType =
            if (isAffiliateShareIcon) ShopPageTrackingConstant.CLICK_SHARE_AFFILIATE_ICON else ShopPageTrackingConstant.CLICK_SHARE_REGULER
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_COMMUNICATION,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_SHARE_BUTTON,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.ETALASE_SHOP,
            ShopPageTrackingConstant.EVENT_LABEL to "$shopId - $shareType - $etalaseId",
            ShopPageTrackingConstant.TRACKER_ID to ShopPageTrackingConstant.TRACKER_ID_CLICK_SHARE_IN_SHOP_ETALASE,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
            ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to shopId,
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickCloseBottomSheetShareEtalase(
        shopId: String, etalaseId: String,
        isAffiliateShareIcon: Boolean, userId: String,
    ) {
        val shareType =
            if (isAffiliateShareIcon) ShopPageTrackingConstant.CLICK_SHARE_AFFILIATE_ICON else ShopPageTrackingConstant.CLICK_SHARE_REGULER
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_COMMUNICATION,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_CLOSE_SHARE_BOTTOM_SHEET,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.ETALASE_SHOP,
            ShopPageTrackingConstant.EVENT_LABEL to "$shopId - $shareType - $etalaseId",
            ShopPageTrackingConstant.TRACKER_ID to ShopPageTrackingConstant.TRACKER_ID_CLICK_CLOSE_SHARE_IN_SHOP_ETALASE,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
            ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to shopId,
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun impressionUniversalBottomSheetEtalase(
        shopId: String, etalaseId: String,
        isAffiliateShareIcon: Boolean, userId: String,
    ) {
        val shareType =
            if (isAffiliateShareIcon) ShopPageTrackingConstant.CLICK_SHARE_AFFILIATE_ICON else ShopPageTrackingConstant.CLICK_SHARE_REGULER
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.VIEW_COMMUNICATION_IRIS,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.VIEW_SHARE_BOTTOM_SHEET,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.ETALASE_SHOP,
            ShopPageTrackingConstant.EVENT_LABEL to "$shopId - $shareType - $etalaseId",
            ShopPageTrackingConstant.TRACKER_ID to ShopPageTrackingConstant.TRACKER_ID_IMPRESSION_UNIVERSAL_SHARE_IN_SHOP_ETALASE,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
            ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to shopId,
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickShareBottomSheetOption(
        socialMediaName: String,
        shopId: String,
        userId: String,
        etalaseId: String,
        imageType: String,
        userType: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_COMMUNICATION,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_SHARE_BOTTOM_SHEET_OPTION,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.ETALASE_SHOP,
            ShopPageTrackingConstant.EVENT_LABEL to "$socialMediaName - $shopId - $userType - $etalaseId - $imageType",
            ShopPageTrackingConstant.TRACKER_ID to ShopPageTrackingConstant.TRACKER_ID_CLICK_SHARE_OPTION_SHOP_ETALASE,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
            ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to shopId,
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

}
