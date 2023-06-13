package com.tokopedia.tkpd.flashsale.util.tracker

import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.EVENT
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.Key.TRACKER_ID
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AddChooseProductTracker @Inject constructor(private val userSession: UserSessionInterface) {

    companion object {
        private const val EVENT_CATEGORY = "flash sale - tambah produk"
    }

    // Click "Tambah Produk" Event
    fun sendClickAddProductEvent(campaignId: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click tambah produk")
            .setEventCategory(EVENT_CATEGORY)
            .setEventLabel(campaignId)
            .setCustomProperty(TRACKER_ID, "37214")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    // Click "Cek Detail" Event
    fun sendClickDetailCheckAllIneligibleLocationOrVariantEvent(campaignId: String, productId: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction("click cek detail - all ineligible location or variant")
            .setEventCategory(EVENT_CATEGORY)
            .setEventLabel("$campaignId - $productId")
            .setCustomProperty(TRACKER_ID, "37215")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

}
