package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia://hotel/dashboard"
 */

object ApplinkConstInternalTravel {

    @JvmField
    val HOST_HOTEL = "hotel"
    @JvmField
    val HOST_FLIGHT = "pesawat"
    @JvmField
    val HOST_TRAVEL_HOMEPAGE = "travelentertainment"

    @JvmField
    val INTERNAL_HOTEL = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://$HOST_HOTEL"
    @JvmField
    val INTERNAL_FLIGHT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_FLIGHT"
    @JvmField
    val INTERNAL_TRAVEL_HOMEPAGE = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://$HOST_TRAVEL_HOMEPAGE"

    @JvmField
    val DASHBOARD_HOTEL = "$INTERNAL_HOTEL/dashboard"

    @JvmField
    val HOTEL_PROMO_LIST = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://promoNative?menuID=4&categoryID=742"

    @JvmField
    val DASHBOARD_FLIGHT = "$INTERNAL_FLIGHT/dashboard"
    @JvmField
    val CANCELLATION_FLIGHT = "$INTERNAL_FLIGHT/cancel"

    @JvmField
    val HOME_TRAVEL_HOMEPAGE = "$INTERNAL_TRAVEL_HOMEPAGE/home"
    @JvmField
    val CITY_PAGE_TRAVEL_HOMEPAGE = "$INTERNAL_TRAVEL_HOMEPAGE/home/city"

    @JvmField
    val EXTRA_DESTINATION_WEB_URL = "EXTRA_DESTINATION_WEB_URL"

}
