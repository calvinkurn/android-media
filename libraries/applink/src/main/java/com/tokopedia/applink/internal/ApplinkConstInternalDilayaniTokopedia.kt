package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * Created by irpan on 26/09/22.
 */
object ApplinkConstInternalDilayaniTokopedia {

    const val HOST_DILAYANI_TOKOPEDIA = "dilayani-tokopedia"

    private const val INTERNAL_DILAYANI_TOKOPEDIA = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_DILAYANI_TOKOPEDIA"

    const val HOME = "$INTERNAL_DILAYANI_TOKOPEDIA/home"
    const val SEARCH = "$INTERNAL_DILAYANI_TOKOPEDIA/search"
    const val DILAYANI_TOKOPEDIA_DISCOVERY_PAGENAME = "dilayani-tokopedia"
}
