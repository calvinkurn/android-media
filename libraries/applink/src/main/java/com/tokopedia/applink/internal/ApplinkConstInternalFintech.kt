package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://marketplace".
 */
object ApplinkConstInternalFintech {

    const val HOST_FINTECH = "fintech"
    val INTERNAL_FINTECH = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_FINTECH}"
    @JvmField
    val PAYLATER = "$INTERNAL_FINTECH/paylater"

}
