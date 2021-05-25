package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia://hotel/dashboard"
 */

object ApplinkConstInternalTravel {

    const val HOST_HOTEL = "hotel"
    const val HOST_FLIGHT = "pesawat"
    const val HOST_TRAVEL_HOMEPAGE = "travelentertainment"
    const val HOST_TRAVEL_SLICE = "travelslice"

    const val INTERNAL_HOTEL = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://$HOST_HOTEL"
    const val INTERNAL_FLIGHT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_FLIGHT"
    const val INTERNAL_TRAVEL_HOMEPAGE = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://$HOST_TRAVEL_HOMEPAGE"

    const val DASHBOARD_HOTEL = "$INTERNAL_HOTEL/dashboard"

    const val HOTEL_OLD_SRP = "$INTERNAL_HOTEL/result/oldsrp"

    const val HOTEL_MAP_SRP = "$INTERNAL_HOTEL/result/map"

    const val HOTEL_DETAIL = "$INTERNAL_HOTEL/detail"

    const val HOTEL_PROMO_LIST = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://promoNative?menuID=4&categoryID=742"

    const val DASHBOARD_FLIGHT = "$INTERNAL_FLIGHT/dashboard"
    const val CANCELLATION_FLIGHT = "$INTERNAL_FLIGHT/cancellation"

    const val HOME_TRAVEL_HOMEPAGE = "$INTERNAL_TRAVEL_HOMEPAGE/home"
    const val CITY_PAGE_TRAVEL_HOMEPAGE = "$INTERNAL_TRAVEL_HOMEPAGE/home/city"

    const val TRAIN_ORDER_LIST = "tokopedia://webview?url=https://m.tokopedia.com/order-list?category=train&allow_override=false"

    const val EXTRA_DESTINATION_WEB_URL = "EXTRA_DESTINATION_WEB_URL"

    const val TRAVEL_ACTION = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_TRAVEL_SLICE/main"
}
