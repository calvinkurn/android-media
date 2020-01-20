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
    val INTERNAL_HOTEL = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://$HOST_HOTEL"
    @JvmField
    val INTERNAL_FLIGHT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_FLIGHT"

    @JvmField
    val DASHBOARD_HOTEL = "$INTERNAL_HOTEL/dashboard"

    @JvmField
    val DASHBOARD_FLIGHT = "$INTERNAL_FLIGHT/dashboard"
    @JvmField
    val CANCELLATION_FLIGHT = "$INTERNAL_FLIGHT/cancel"

}
