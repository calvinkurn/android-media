package com.tokopedia.travel_slice.analytics

import android.content.Context
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by jessica on 01/12/20
 */

class TravelSliceAnalytics @Inject constructor(private val userSession: UserSessionInterface) {

    private fun getIrisSessionId(context: Context): String = IrisSession(context).getSessionId()

    fun viewFlightReservation(context: Context, isLogin: Boolean, isSuccess: Boolean) {
        val loginStatus  = if (isLogin) "login" else  "non - login"
        val successStatus = if (isSuccess) "success" else "failed"
        sendGeneralFlightTracking(
                context,
                eventName = TravelSliceAnalyticsConst.Event.VIEW_FLIGHT_IRIS,
                eventAction = TravelSliceAnalyticsConst.EventAction.FLIGHT_RESERVATION_IMPRESSION,
                eventLabel = "$successStatus - $loginStatus")
    }

    fun clickOnFlightReservation(context: Context, userId: String) {
        sendGeneralFlightTracking(
                context,
                eventName = TravelSliceAnalyticsConst.Event.CLICK_FLIGHT,
                eventAction = TravelSliceAnalyticsConst.EventAction.FLIGHT_RESERVATION_CLICK,
                eventLabel = userId)
    }

    fun viewHotelReservation(context: Context, isSuccess: Boolean, isLogin: Boolean) {
        val loginStatus  = if (isLogin) "login" else  "non - login"
        val successStatus = if (isSuccess) "success" else "failed"
        sendGeneralHotelTracking(
                context,
                eventName = TravelSliceAnalyticsConst.Event.VIEW_HOTEL_IRIS,
                eventAction = TravelSliceAnalyticsConst.EventAction.HOTEL_RESERVATION_IMPRESSION,
                eventLabel = "$successStatus - $loginStatus")
    }


    fun clickOnHotelReservation(context: Context, city: String, userId: String) {
        sendGeneralHotelTracking(
                context,
                eventName = TravelSliceAnalyticsConst.Event.CLICK_HOTEL,
                eventAction = TravelSliceAnalyticsConst.EventAction.HOTEL_RESERVATION_CLICK,
                eventLabel = "$city - $userId")
    }

    fun viewHotelBooking(context: Context, isSuccess: Boolean, isLogin: Boolean) {
        val loginStatus  = if (isLogin) "login" else  "non - login"
        val successStatus = if (isSuccess) "success" else "failed"
        sendGeneralHotelTracking(
                context,
                eventName = TravelSliceAnalyticsConst.Event.VIEW_HOTEL_IRIS,
                eventAction = TravelSliceAnalyticsConst.EventAction.HOTEL_BOOK_IMPRESSION,
                eventLabel = "$successStatus - $loginStatus")
    }

    fun clickOnHotelBooking(context: Context, city: String, userId: String) {
        sendGeneralHotelTracking(
                context,
                eventName = TravelSliceAnalyticsConst.Event.CLICK_HOTEL,
                eventAction = TravelSliceAnalyticsConst.EventAction.HOTEL_BOOK_CLICK,
                eventLabel = "$city - $userId")
    }

    private fun sendGeneralFlightTracking(context: Context, eventName: String, eventAction: String, eventLabel: String) {
        val trackingMap = mapOf(
                TravelSliceAnalyticsConst.Key.EVENT to eventName,
                TravelSliceAnalyticsConst.Key.EVENT_CATEGORY to TravelSliceAnalyticsConst.Data.EVENT_CATEGORY_DIGITAL_FLIGHT,
                TravelSliceAnalyticsConst.Key.EVENT_ACTION to eventAction,
                TravelSliceAnalyticsConst.Key.EVENT_LABEL to eventLabel,
                TravelSliceAnalyticsConst.Key.BUSINESS_UNIT to TravelSliceAnalyticsConst.Data.BU_TRAVEL_ENTERTAINMENT,
                TravelSliceAnalyticsConst.Key.CURRENT_SITE to TravelSliceAnalyticsConst.Data.CURRENT_SITE_FLIGHT,
                TravelSliceAnalyticsConst.Key.USER_ID to userSession.userId,
                TravelSliceAnalyticsConst.Key.CLIENT_ID to TrackApp.getInstance().gtm.clientIDString,
                TravelSliceAnalyticsConst.Key.SESSION_IRIS to getIrisSessionId(context),
                TravelSliceAnalyticsConst.Key.SCREEN_NAME to TravelSliceAnalyticsConst.Data.SCREEN_NAME_FLIGHT_OKGOOGLE,
                TravelSliceAnalyticsConst.Key.CATEGORY to TravelSliceAnalyticsConst.Data.CATEGORY_FLIGHT
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(trackingMap)
    }

    private fun sendGeneralHotelTracking(context: Context, eventName: String, eventAction: String, eventLabel: String) {
        val trackingMap = mapOf(
                TravelSliceAnalyticsConst.Key.EVENT to eventName,
                TravelSliceAnalyticsConst.Key.EVENT_CATEGORY to TravelSliceAnalyticsConst.Data.EVENT_CATEGORY_DIGITAL_NATIVE,
                TravelSliceAnalyticsConst.Key.EVENT_ACTION to eventAction,
                TravelSliceAnalyticsConst.Key.EVENT_LABEL to eventLabel,
                TravelSliceAnalyticsConst.Key.BUSINESS_UNIT to TravelSliceAnalyticsConst.Data.BU_TRAVEL_ENTERTAINMENT,
                TravelSliceAnalyticsConst.Key.CURRENT_SITE to TravelSliceAnalyticsConst.Data.CURRENT_SITE_HOTEL,
                TravelSliceAnalyticsConst.Key.USER_ID to userSession.userId,
                TravelSliceAnalyticsConst.Key.SCREEN_NAME to TravelSliceAnalyticsConst.Data.SCREEN_NAME_HOTEL_OKGOOGLE,
                TravelSliceAnalyticsConst.Key.CATEGORY to TravelSliceAnalyticsConst.Data.CATEGORY_HOTEL,
                TravelSliceAnalyticsConst.Key.CLIENT_ID to TrackApp.getInstance().gtm.clientIDString,
                TravelSliceAnalyticsConst.Key.SESSION_IRIS to getIrisSessionId(context)
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(trackingMap)
    }
}