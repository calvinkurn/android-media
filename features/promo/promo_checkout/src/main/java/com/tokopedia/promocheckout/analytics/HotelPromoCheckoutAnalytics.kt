package com.tokopedia.promocheckout.analytics

import android.content.Context
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.promocheckout.list.view.activity.PromoCheckoutListHotelActivity.Companion.HOTEL_LABEL
import com.tokopedia.promocheckout.list.view.activity.PromoCheckoutListHotelActivity.Companion.TOKOPEDIA_DIGITAL_HOTEL
import com.tokopedia.promocheckout.list.view.activity.PromoCheckoutListHotelActivity.Companion.TRAVELENTERTAINMENT_LABEL
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSession

class HotelPromoCheckoutAnalytics {
    fun hotelApplyPromo(context: Context?, promoCode: String, screenName: String) {
        val map = mutableMapOf<String, Any>()
        map[SCREEN_NAME] = screenName
        map[CURRENT_SITE] = TOKOPEDIA_DIGITAL_HOTEL
        map[CLIENT_ID] = TrackApp.getInstance().gtm.clientIDString ?: ""
        map[SESSION_IRIS] = if (context != null) IrisSession(context).getSessionId() else ""
        map[USER_ID] = if (context != null) UserSession(context).userId else ""
        map[BUSINESS_UNIT] = TRAVELENTERTAINMENT_LABEL
        map[CATEGORY_LABEL] = HOTEL_LABEL
        map[EVENT] = CLICK_HOTEL
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = APPLY_PROMO
        map[EVENT_LABEL] = "$HOTEL_LABEL - $promoCode"
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    companion object{
        const val SCREEN_NAME = "screenName"
        const val CURRENT_SITE = "currentSite"
        const val CLIENT_ID = "clientId"
        const val SESSION_IRIS = "sessionIris"
        const val USER_ID = "userId"
        const val BUSINESS_UNIT = "businessUnit"
        const val CATEGORY_LABEL = "category"
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"
        const val DIGITAL_NATIVE = "digital - native"
        const val APPLY_PROMO = "apply promo"
        const val CLICK_HOTEL = "clickHotel"

        const val HOTEL_BOOKING_SCREEN_NAME = "/hotel/bookingroom"
    }
}