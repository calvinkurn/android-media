package com.tokopedia.settingnotif.usersetting.analytics

import android.os.Bundle
import com.tokopedia.track.TrackApp

object NotifSettingAnalytics {

    private const val EVENT_CLICK_NC = "clickNotifCenter"
    private const val EVENT_CATEGORY = "notif center"
    private const val EVENT_ACTION_CLICK_TS_BTN = "click on check push notifikasi di hp mu"

    fun trackTroubleshooterClicked(userId: String, shopId: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                EVENT_CLICK_NC,
                Bundle().apply {
                    putString("eventCategory", EVENT_CATEGORY)
                    putString("eventAction", EVENT_ACTION_CLICK_TS_BTN)
                    putString("eventLabel", "")
                    putString("userId", userId)
                    putString("shopId", shopId)
                }
        )
    }


}