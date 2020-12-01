package com.tokopedia.travel_slice.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by jessica on 01/12/20
 */

class TravelSliceAnalytics @Inject constructor(private val userSession: UserSessionInterface) {

    private fun userClickOnHotelDashboard() {
        sendGeneralTracking("clickdashboard","clickdashboard")
    }

    private fun sendGeneralTracking(eventName: String, eventAction: String) {
        val trackingMap = mapOf(
                TravelSliceAnalyticsConst.Key.EVENT to eventName,
                TravelSliceAnalyticsConst.Key.EVENT_CATEGORY to "xx",
                TravelSliceAnalyticsConst.Key.EVENT_ACTION to eventAction,
                TravelSliceAnalyticsConst.Key.EVENT_LABEL to "",
                TravelSliceAnalyticsConst.Key.BUSINESS_UNIT to "xx",
                TravelSliceAnalyticsConst.Key.CURRENT_SITE to "xx",
                TravelSliceAnalyticsConst.Key.USER_ID to userSession.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(trackingMap)
    }
}