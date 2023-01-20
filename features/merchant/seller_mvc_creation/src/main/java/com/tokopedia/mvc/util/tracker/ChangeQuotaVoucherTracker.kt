package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.mvc.util.constant.TrackerConstant.ChangeQuota.event
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ChangeQuotaVoucherTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickCloseEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click close - edit quota")
            .setEventCategory(event)
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "39445")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickOkEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click oke - edit quota")
            .setEventCategory(event)
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "39446")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickResetEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click reset - edit quota")
            .setEventCategory(event)
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "39447")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickOptionsEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click kupon options - edit quota")
            .setEventCategory(event)
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "39448")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
