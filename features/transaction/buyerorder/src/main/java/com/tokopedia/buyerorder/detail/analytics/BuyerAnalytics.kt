package com.tokopedia.buyerorder.detail.analytics

import android.app.Activity
import com.tokopedia.track.TrackApp

/**
 * Created by fwidjaja on 2019-11-29.
 */
object BuyerAnalytics {

    @JvmStatic
    fun sendScreenName(activity: Activity, screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }
}