package com.tokopedia.tkpd.flashsale.util.tracker

import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.EVENT
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.EventActionValue.CLICK_APPLY_PRODUCT_DISCOUNT
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.EventActionValue.CLICK_MANAGE_PRODUCT_DISCOUNT
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.EventCategoryValue.EVENT_CATEGORY_FLASH_SALE_MANAGE_PRODUCT
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.Key.TRACKER_ID
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.TrackerIdValue.TRACKER_ID_APPLY_MANAGE_PRODUCT_DISCOUNT
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.TrackerIdValue.TRACKER_ID_CLICK_MANAGE_PRODUCT_DISCOUNT
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

//https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3486 -> No 12-13
class FlashSaleManageProductListPageTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickManageProductDiscountEvent (campaignId: String, productId: String) {
        val eventLabel = "$campaignId - $productId"
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction(CLICK_MANAGE_PRODUCT_DISCOUNT)
            .setEventCategory(EVENT_CATEGORY_FLASH_SALE_MANAGE_PRODUCT)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_MANAGE_PRODUCT_DISCOUNT)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickApplyManageDiscountEvent (campaignId: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction(CLICK_APPLY_PRODUCT_DISCOUNT)
            .setEventCategory(EVENT_CATEGORY_FLASH_SALE_MANAGE_PRODUCT)
            .setEventLabel(campaignId)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_APPLY_MANAGE_PRODUCT_DISCOUNT)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
