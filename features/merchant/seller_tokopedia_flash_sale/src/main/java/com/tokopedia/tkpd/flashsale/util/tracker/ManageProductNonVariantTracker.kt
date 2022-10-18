package com.tokopedia.tkpd.flashsale.util.tracker

import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.EVENT
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.Key.TRACKER_ID
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ManageProductNonVariantTracker @Inject constructor(private val userSession: UserSessionInterface) {

    companion object {
        private const val EVENT_CATEGORY = "flash sale - atur produk"
    }

    // Fill in column price
    fun sendClickFillInCampaignPriceEvent(
        campaignId: String,
        productId: String,
        locationType: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click fill in campaign price")
            .setEventCategory(EVENT_CATEGORY)
            .setEventLabel("$campaignId - $productId - $locationType")
            .setCustomProperty(TRACKER_ID, "37218")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    // Fill in column percentage
    fun sendClickFillInCampaignDiscountPercentageEvent(
        campaignId: String,
        productId: String,
        locationType: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click fill in campaign discount percentage")
            .setEventCategory(EVENT_CATEGORY)
            .setEventLabel("$campaignId - $productId - $locationType")
            .setCustomProperty(TRACKER_ID, "37219")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    // Click "Simpan" button
    fun sendClickSaveEvent(
        campaignId: String,
        productId: String,
        locationType: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click simpan")
            .setEventCategory(EVENT_CATEGORY)
            .setEventLabel("$campaignId - $productId - $locationType")
            .setCustomProperty(TRACKER_ID, "37221")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    // Toggle location switch
    fun sendClickAdjustToggleLocationEvent(
        campaignId: String,
        productId: String,
        locationType: String,
        warehouseId: String,
    ) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click adjust toggle location")
            .setEventCategory(EVENT_CATEGORY)
            .setEventLabel("$campaignId - $productId - $locationType - $warehouseId")
            .setCustomProperty(TRACKER_ID, "37222")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    // Click "Cek Detail" Button
    fun sendClickDetailCheckPartialIneligibleLocationEvent(
        campaignId: String,
        productId: String,
        locationType: String,
        warehouseId: String,
    ) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click cek detail - partial ineligible location")
            .setEventCategory(EVENT_CATEGORY)
            .setEventLabel("$campaignId - $productId - $locationType - $warehouseId")
            .setCustomProperty(TRACKER_ID, "37223")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

}
