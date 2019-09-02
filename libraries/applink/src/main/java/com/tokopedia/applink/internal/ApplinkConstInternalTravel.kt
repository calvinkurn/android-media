package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia://hotel/dashboard"
 */

object ApplinkConstInternalTravel {

    @JvmField
    val HOST_HOTEL = "hotel"

    @JvmField
    val INTERNAL_HOTEL = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://${HOST_HOTEL}"

    @JvmField
    val DASHBOARD_HOTEL = "${INTERNAL_HOTEL}/dashboard"

}
