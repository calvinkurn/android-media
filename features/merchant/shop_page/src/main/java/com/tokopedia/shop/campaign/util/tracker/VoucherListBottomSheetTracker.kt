package com.tokopedia.shop.campaign.util.tracker

import com.tokopedia.shop.analytic.ShopPageTrackingConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Tracker: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4008
 */
class VoucherListBottomSheetTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendVoucherDetailBottomSheetImpression(shopId: String) {
        Tracker.Builder()
            .setEvent(ShopPageTrackingConstant.Event.VIEW_PG_IRIS)
            .setEventAction(ShopPageTrackingConstant.EventAction.CAMPAIGN_TAB_COUPON_LIST_IMPRESSION)
            .setEventCategory(ShopPageTrackingConstant.EventCategory.SHOP_PAGE_BUYER)
            .setEventLabel("")
            .setCustomProperty(ShopPageTrackingConstant.TRACKER_ID, ShopPageTrackingConstant.TrackerId.TRACKER_ID_EXCLUSIVE_LAUNCH_VOUCHER_LIST_IMPRESSION)
            .setBusinessUnit(ShopPageTrackingConstant.PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }
}
