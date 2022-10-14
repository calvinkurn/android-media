package com.tokopedia.tkpd.flashsale.util.tracker

import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CampaignDetailPageTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickSeeCriteriaEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click lihat kriteria")
            .setEventCategory("flash sale - campaign detail")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37211")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickRegisterEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click daftar")
            .setEventCategory("flash sale - campaign detail")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37212")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickCheckReasonEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click cek alasan")
            .setEventCategory("flash sale - campaign detail")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "37213")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
