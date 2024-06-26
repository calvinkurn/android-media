package com.tokopedia.mvc.util.tracker

import com.tokopedia.mvc.util.constant.TrackerConstant.EVENT
import com.tokopedia.mvc.util.constant.TrackerConstant.TRACKER_ID
import com.tokopedia.mvc.util.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.mvc.util.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class QuotaInfoTracker @Inject constructor(private val userSession: UserSessionInterface) {
    companion object {
        private const val EA_CLICK_CLOSE = "click close"
        private const val EA_CLICK_BUTTON_UPGRADE = "click button upgrade"
        private const val EC_QUOTA_INFO_PAGE = "kupon toko saya - sumber kuota"
    }

    fun sendClickCloseEvent() {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction(EA_CLICK_CLOSE)
            .setEventCategory(EC_QUOTA_INFO_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, "39433")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }

    fun sendClickButtonUpgradeEvent(buttonName: String) {
        Tracker.Builder()
            .setEvent(EVENT)
            .setEventAction(EA_CLICK_BUTTON_UPGRADE)
            .setEventCategory(EC_QUOTA_INFO_PAGE)
            .setEventLabel(buttonName)
            .setCustomProperty(TRACKER_ID, "39434")
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .setShopId(userSession.shopId)
            .build()
            .send()
    }
}
