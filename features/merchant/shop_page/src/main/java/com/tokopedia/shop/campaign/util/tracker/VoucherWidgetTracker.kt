package com.tokopedia.shop.campaign.util.tracker

import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Tracker: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4008
 */
class VoucherWidgetTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendVoucherImpression(shopId: String, campaignId: String, widgetId: String) {
        val eventLabel = "$campaignId - $widgetId"

        Tracker.Builder()
            .setEvent(ShopPageTrackingConstant.Event.VIEW_PG_IRIS)
            .setEventAction(ShopPageTrackingConstant.EventAction.CAMPAIGN_TAB_COUPON_IMPRESSION)
            .setEventCategory(ShopPageTrackingConstant.EventCategory.SHOP_PAGE_BUYER)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                ShopPageTrackingConstant.TRACKER_ID,
                ShopPageTrackingConstant.TrackerId.TRACKER_ID_EXCLUSIVE_LAUNCH_VOUCHER_IMPRESSION
            )
            .setBusinessUnit(ShopPageTrackingConstant.PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }


    fun sendUnredeemedVoucherClickEvent(shopId: String, campaignId: String, widgetId: String) {
        val eventLabel = "klaim - $campaignId - $widgetId}"

        Tracker.Builder()
            .setEvent(ShopPageTrackingConstant.CLICK_PG)
            .setEventAction(ShopPageTrackingConstant.EventAction.CAMPAIGN_TAB_COUPON_CLICK)
            .setEventCategory(ShopPageTrackingConstant.EventCategory.SHOP_PAGE_BUYER)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                ShopPageTrackingConstant.TRACKER_ID,
                ShopPageTrackingConstant.TrackerId.TRACKER_ID_EXCLUSIVE_LAUNCH_VOUCHER_CLICK_ON_CAMPAIGN_TAB
            )
            .setBusinessUnit(ShopPageTrackingConstant.PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendRedeemedVoucherClickEvent(shopId: String, campaignId: String, widgetId: String) {
        val eventLabel = "pakai - $campaignId - $widgetId}"

        Tracker.Builder()
            .setEvent(ShopPageTrackingConstant.CLICK_PG)
            .setEventAction(ShopPageTrackingConstant.EventAction.CAMPAIGN_TAB_COUPON_CLICK)
            .setEventCategory(ShopPageTrackingConstant.EventCategory.SHOP_PAGE_BUYER)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                ShopPageTrackingConstant.TRACKER_ID,
                ShopPageTrackingConstant.TrackerId.TRACKER_ID_EXCLUSIVE_LAUNCH_VOUCHER_CLICK_ON_CAMPAIGN_TAB
            )
            .setBusinessUnit(ShopPageTrackingConstant.PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

}
