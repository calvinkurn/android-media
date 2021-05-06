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
    val HOST_TRAVEL_SLICE = "travelslice"

    @JvmField
    val INTERNAL_HOTEL = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://$HOST_HOTEL"
    @JvmField
    val INTERNAL_FLIGHT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_FLIGHT"
    @JvmField
    val INTERNAL_TRAVEL_HOMEPAGE = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://$HOST_TRAVEL_HOMEPAGE"

    @JvmField
    val DASHBOARD_HOTEL = "$INTERNAL_HOTEL/dashboard"

    @JvmField
    val HOTEL_OLD_SRP = "$INTERNAL_HOTEL/result/oldsrp"

    @JvmField
    val HOTEL_MAP_SRP = "$INTERNAL_HOTEL/result/map"

    @JvmField
    val HOTEL_DETAIL = "$INTERNAL_HOTEL/detail"

    @JvmField
    val HOTEL_PROMO_LIST = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://promoNative?menuID=4&categoryID=742"

    @JvmField
    val DASHBOARD_FLIGHT = "$INTERNAL_FLIGHT/dashboard"
    @JvmField
    val CANCELLATION_FLIGHT = "$INTERNAL_FLIGHT/cancellation"

    @JvmField
    val HOME_TRAVEL_HOMEPAGE = "$INTERNAL_TRAVEL_HOMEPAGE/home"
    @JvmField
    val CITY_PAGE_TRAVEL_HOMEPAGE = "$INTERNAL_TRAVEL_HOMEPAGE/home/city"

    @JvmField
    val TRAIN_ORDER_LIST = "tokopedia://webview?url=https://m.tokopedia.com/order-list?category=train&allow_override=false"

    @JvmField
    val EXTRA_DESTINATION_WEB_URL = "EXTRA_DESTINATION_WEB_URL"

    @JvmField
    val TRAVEL_ACTION = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_TRAVEL_SLICE/main"
}
