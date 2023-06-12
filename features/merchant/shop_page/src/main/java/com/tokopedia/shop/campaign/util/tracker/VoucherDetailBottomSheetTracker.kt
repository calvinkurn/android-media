package com.tokopedia.shop.campaign.util.tracker

import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Tracker: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4008
 */
class VoucherDetailBottomSheetTracker @Inject constructor(private val userSession: UserSessionInterface) {


    fun sendVoucherDetailBottomSheetImpression(
        campaignId: String,
        widgetId: String,
        shopId: String
    ) {
        val eventLabel = "$campaignId - $widgetId"

        Tracker.Builder()
            .setEvent(ShopPageTrackingConstant.Event.VIEW_PG_IRIS)
            .setEventAction(ShopPageTrackingConstant.EventAction.CAMPAIGN_TAB_COUPON_DETAIL_IMPRESSION)
            .setEventCategory(ShopPageTrackingConstant.EventCategory.SHOP_PAGE_BUYER)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                ShopPageTrackingConstant.TRACKER_ID,
                ShopPageTrackingConstant.TrackerId.TRACKER_ID_EXCLUSIVE_LAUNCH_VOUCHER_DETAIL_IMPRESSION
            )
            .setBusinessUnit(ShopPageTrackingConstant.PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }


    fun sendRedeemVoucherEvent(campaignId: String, shopId: String) {
        val eventLabel = "klaim - $campaignId"

        Tracker.Builder()
            .setEvent(ShopPageTrackingConstant.CLICK_PG)
            .setEventAction(ShopPageTrackingConstant.EventAction.CAMPAIGN_TAB_COUPON_DETAIL_CLICK)
            .setEventCategory(ShopPageTrackingConstant.EventCategory.SHOP_PAGE_BUYER)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                ShopPageTrackingConstant.TRACKER_ID,
                ShopPageTrackingConstant.TrackerId.TRACKER_ID_EXCLUSIVE_LAUNCH_VOUCHER_CLICK
            )
            .setBusinessUnit(ShopPageTrackingConstant.PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    fun sendUseVoucherEvent(campaignId: String, shopId: String) {
        val eventLabel = "gunakan - $campaignId"

        Tracker.Builder()
            .setEvent(ShopPageTrackingConstant.CLICK_PG)
            .setEventAction(ShopPageTrackingConstant.EventAction.CAMPAIGN_TAB_COUPON_DETAIL_CLICK)
            .setEventCategory(ShopPageTrackingConstant.EventCategory.SHOP_PAGE_BUYER)
            .setEventLabel(eventLabel)
            .setCustomProperty(
                ShopPageTrackingConstant.TRACKER_ID,
                ShopPageTrackingConstant.TrackerId.TRACKER_ID_EXCLUSIVE_LAUNCH_VOUCHER_CLICK
            )
            .setBusinessUnit(ShopPageTrackingConstant.PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }
}
