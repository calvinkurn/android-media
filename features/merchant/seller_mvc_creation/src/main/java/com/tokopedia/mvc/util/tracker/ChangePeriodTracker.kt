package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ChangePeriodTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickCloseEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click close - edit period")
            .setEventCategory(TrackerConstant.ChangePeriod.event)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39449")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickResetEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click reset - edit period")
            .setEventCategory(TrackerConstant.ChangePeriod.event)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39450")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickOkEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click oke - edit period")
            .setEventCategory(TrackerConstant.ChangePeriod.event)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39451")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickStartEndDateEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click start end date - edit period")
            .setEventCategory(TrackerConstant.ChangePeriod.event)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackerConstant.TRACKER_ID, "39452")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
