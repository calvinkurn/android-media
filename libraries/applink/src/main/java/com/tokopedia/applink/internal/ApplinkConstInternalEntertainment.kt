package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia://hotel/dashboard"
 */

object ApplinkConstInternalEntertainment {

    @JvmField
    val HOST_ENTERTAINMENT = "entertainment"

    @JvmField
    val INTERNAL_ENTERTAINMENT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_ENTERTAINMENT"

    @JvmField
    val ENTERTAINMENT_HOME = "$INTERNAL_ENTERTAINMENT/home"

}
