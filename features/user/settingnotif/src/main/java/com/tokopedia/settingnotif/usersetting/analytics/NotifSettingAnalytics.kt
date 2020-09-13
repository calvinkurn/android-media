package com.tokopedia.settingnotif.usersetting.analytics

import com.tokopedia.track.TrackApp

object NotifSettingAnalytics {

    private const val EVENT_CLICK_NC = "clickNotifCenter"
    private const val EVENT_CATEGORY = "notif center"
    private const val EVENT_ACTION_CLICK_TS_BTN = "click on check push notifikasi di hp mu"

    fun trackTroubleshooterClicked(userId: String, shopId: String) {
        val data = mapOf(
                "event" to EVENT_CLICK_NC,
                "eventCategory" to EVENT_CATEGORY,
                "eventAction" to EVENT_ACTION_CLICK_TS_BTN,
                "eventLabel" to "",
                "userId" to userId,
                "shopId" to shopId
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(data)
    }


}