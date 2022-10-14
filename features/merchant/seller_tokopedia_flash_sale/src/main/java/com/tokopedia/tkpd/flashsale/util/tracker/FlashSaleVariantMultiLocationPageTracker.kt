package com.tokopedia.tkpd.flashsale.util.tracker

import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class FlashSaleVariantMultiLocationPageTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickAturSekaligusEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click atur sekaligus")
            .setEventCategory("flash sale - atur lokasi")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37234")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendAdjustToggleLokasiEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click adjust toggle lokasi")
            .setEventCategory("flash sale - atur lokasi")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37235")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendFillinCampaignPriceEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click fill in campaign price")
            .setEventCategory("flash sale - atur lokasi")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37236")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendFillinCampaignDiscountEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click fill in campaign discount percentage")
            .setEventCategory("flash sale - atur lokasi")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37237")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendEditCampaignStockEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click edit campaign stock")
            .setEventCategory("flash sale - atur lokasi")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37240")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendEditCampaignSimpanDiscountEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click simpan")
            .setEventCategory("flash sale - atur lokasi")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37241")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendDetailPartialIneligibleDiscountEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click cek detail - partial ineligible location")
            .setEventCategory("flash sale - atur lokasi")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37243")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
