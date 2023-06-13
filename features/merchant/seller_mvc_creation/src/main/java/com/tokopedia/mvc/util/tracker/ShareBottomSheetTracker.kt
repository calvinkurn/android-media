package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.TrackerConstant
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShareBottomSheetTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickBroadcastChatEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click broadcast chat")
            .setEventCategory("kupon toko saya - detail kupon - bagikan kupon")
            .setEventLabel("")
            .setCustomProperty("trackerId", "40649")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickShareToSocialMediaEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackerConstant.EVENT)
            .setEventAction("click bagikan media icon")
            .setEventCategory("kupon toko saya - detail kupon - bagikan kupon")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "40650")
            .setBusinessUnit(TrackerConstant.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstant.CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
