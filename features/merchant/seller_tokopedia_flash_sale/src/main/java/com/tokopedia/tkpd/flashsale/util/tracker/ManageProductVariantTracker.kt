package com.tokopedia.tkpd.flashsale.util.tracker

import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ManageProductVariantTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickManageAllEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click atur sekaligus")
            .setEventCategory("flash sale - atur varian")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37224")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendAdjustToggleVariantEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click adjust toggle variant")
            .setEventCategory("flash sale - atur varian")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37225")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFillInCampaignPriceEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click fill in campaign price")
            .setEventCategory("flash sale - atur varian")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37228")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickFillInDiscountPercentageEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click fill in campaign discount percentage")
            .setEventCategory("flash sale - atur varian")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37229")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickEditCampaignStockEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click edit campaign stock")
            .setEventCategory("flash sale - atur varian")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37230")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickSaveEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click simpan")
            .setEventCategory("flash sale - atur varian")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37231")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
