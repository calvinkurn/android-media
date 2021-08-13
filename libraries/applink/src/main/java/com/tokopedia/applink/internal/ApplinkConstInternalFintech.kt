package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://marketplace".
 */
object ApplinkConstInternalFintech {

    private const val HOST_FINTECH = "fintech"
    private const val INTERNAL_FINTECH = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_FINTECH}"
    const val PAYLATER = "$INTERNAL_FINTECH/paylater"

}
