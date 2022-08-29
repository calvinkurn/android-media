package com.tokopedia.vouchercreation.common.tracker

import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import javax.inject.Inject

class SharingComponentTracker @Inject constructor(private val userSession : UserSessionInterface) {


    fun sendShareClickEvent(entryPoint : String, couponId : String) {
        val payload = mapOf(
            VoucherCreationAnalyticConstant.Key.EVENT to "clickCommunication",
            VoucherCreationAnalyticConstant.Key.EVENT_ACTION to "click - share button",
            VoucherCreationAnalyticConstant.Key.EVENT_CATEGORY to "shop page - mvc locked product",
            VoucherCreationAnalyticConstant.Key.EVENT_LABEL to "$entryPoint - $couponId",
            VoucherCreationAnalyticConstant.Key.BUSINESS_UNIT to VoucherCreationAnalyticConstant.Values.SHARING_EXPERIENCE,
            VoucherCreationAnalyticConstant.Key.CURRENT_SITE to VoucherCreationAnalyticConstant.Values.TOKOPEDIA_MARKETPLACE,
            VoucherCreationAnalyticConstant.Key.SHOP_ID to userSession.shopId,
            VoucherCreationAnalyticConstant.Key.USER_ID to userSession.userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(payload)
    }

    fun sendShareBottomSheetDismissClickEvent(couponId : String) {
        sendGeneralTracking(
            event = "clickCommunication",
            action = "click - close share bottom sheet",
            category = "shop page - mvc locked product",
            label = couponId,
            businessUnit = VoucherCreationAnalyticConstant.Values.SHARING_EXPERIENCE,
            currentSite = VoucherCreationAnalyticConstant.Values.TOKOPEDIA_MARKETPLACE,
            userId = userSession.userId,
            shopId = userSession.shopId
        )
    }

    fun sendSelectShareChannelClickEvent(selectedChannel: String, couponId : String) {
        sendGeneralTracking(
            event = "clickCommunication",
            action = "click - sharing channel",
            category = "shop page - mvc locked product",
            label = "$selectedChannel - $couponId",
            businessUnit = VoucherCreationAnalyticConstant.Values.SHARING_EXPERIENCE,
            currentSite = VoucherCreationAnalyticConstant.Values.TOKOPEDIA_MARKETPLACE,
            userId = userSession.userId,
            shopId = userSession.shopId
        )
    }


    fun sendShareBottomSheetDisplayedEvent(couponId : String) {
        sendGeneralTracking(
            event = "viewCommunicationIris",
            action = "view on sharing channel",
            category = "shop page - mvc locked product",
            label = couponId,
            businessUnit = VoucherCreationAnalyticConstant.Values.SHARING_EXPERIENCE,
            currentSite = VoucherCreationAnalyticConstant.Values.TOKOPEDIA_MARKETPLACE,
            userId = userSession.userId,
            shopId = userSession.shopId
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
        userId: String
    ) {
        val payload = mutableMapOf<String, Any>(
            VoucherCreationAnalyticConstant.Key.EVENT to event,
            VoucherCreationAnalyticConstant.Key.EVENT_ACTION to action,
            VoucherCreationAnalyticConstant.Key.EVENT_CATEGORY to category,
            VoucherCreationAnalyticConstant.Key.EVENT_LABEL to label,
            VoucherCreationAnalyticConstant.Key.BUSINESS_UNIT to businessUnit,
            VoucherCreationAnalyticConstant.Key.CURRENT_SITE to currentSite,
            VoucherCreationAnalyticConstant.Key.SHOP_ID to shopId,
            VoucherCreationAnalyticConstant.Key.USER_ID to userId
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(payload)
    }
}