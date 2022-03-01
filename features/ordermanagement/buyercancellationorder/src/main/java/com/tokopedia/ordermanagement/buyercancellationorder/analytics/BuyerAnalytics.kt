package com.tokopedia.ordermanagement.buyercancellationorder.analytics

import com.tokopedia.track.TrackApp

/**
 * Created by fwidjaja on 2019-11-29.
 */
object BuyerAnalytics {

    @JvmStatic
    fun sendScreenName(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }
}