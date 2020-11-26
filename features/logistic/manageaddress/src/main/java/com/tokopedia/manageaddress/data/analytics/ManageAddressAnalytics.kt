package com.tokopedia.manageaddress.data.analytics

import android.app.Activity
import com.tokopedia.track.TrackApp

object ManageAddressAnalytics {

    @JvmStatic
    fun sendScreenName(activity: Activity, screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }
}