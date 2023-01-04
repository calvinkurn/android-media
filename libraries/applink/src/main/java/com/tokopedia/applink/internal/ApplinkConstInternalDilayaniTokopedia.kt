package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * Created by irpan on 26/09/22.
 */
object ApplinkConstInternalDilayaniTokopedia {

    @JvmField
    val HOST_DILAYANI_TOKOPEDIA = "dilayani-tokopedia"

    @JvmField
    val INTERNAL_DILAYANI_TOKOPEDIA = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_DILAYANI_TOKOPEDIA"

    // DtHomeActivity
    @JvmField
    val HOME = "$INTERNAL_DILAYANI_TOKOPEDIA/home"

    @JvmField
    val SEARCH = "$INTERNAL_DILAYANI_TOKOPEDIA/search"
}
