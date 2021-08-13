package com.tokopedia.travel_slice.analytics

/**
 * @author by jessica on 01/12/20
 */

object TravelSliceAnalyticsConst {

    object Key {
        internal const val EVENT = "event"
        internal const val EVENT_CATEGORY = "eventCategory"
        internal const val EVENT_ACTION = "eventAction"
        internal const val EVENT_LABEL = "eventLabel"
        internal const val BUSINESS_UNIT = "businessUnit"
        internal const val CURRENT_SITE = "currentSite"
        internal const val USER_ID = "userId"
        internal const val SCREEN_NAME = "screenName"
        internal const val CATEGORY = "category"
        internal const val CLIENT_ID = "clientId"
        internal const val SESSION_IRIS = "sessionIris"
    }

    object Event {
        const val VIEW_HOTEL_IRIS = "viewHotelIris"
        const val CLICK_HOTEL =  "clickHotel"
        const val VIEW_FLIGHT_IRIS = "viewFlightIris"
        const val CLICK_FLIGHT = "clickFlight"
    }

    object EventAction {
        const val FLIGHT_RESERVATION_IMPRESSION = "ok google reservation impression"
        const val FLIGHT_RESERVATION_CLICK = "ok google reservation - flight detail click"
        const val HOTEL_BOOK_IMPRESSION = "hotel - ok google booking impression"
        const val HOTEL_BOOK_CLICK = "hotel - ok google booking hotel click"
        const val HOTEL_RESERVATION_IMPRESSION = "hotel - ok google reservation impression"
        const val HOTEL_RESERVATION_CLICK = "hotel - ok google reservation - hotel detail click"
    }

    object Data {
        const val EVENT_CATEGORY_DIGITAL_NATIVE = "digital - native"
        const val EVENT_CATEGORY_DIGITAL_FLIGHT = "digital - flight"
        const val SCREEN_NAME_HOTEL_OKGOOGLE = "/hotel/okgoogle"
        const val SCREEN_NAME_FLIGHT_OKGOOGLE = "/flight/okgoogle"
        const val CURRENT_SITE_HOTEL = "tokopediadigitalhotel"
        const val CURRENT_SITE_FLIGHT = "tokopediadigitalflight"
        const val BU_TRAVEL_ENTERTAINMENT = "travel & entertainment"
        const val CATEGORY_HOTEL = "hotel"
        const val CATEGORY_FLIGHT = "flight"
    }

}