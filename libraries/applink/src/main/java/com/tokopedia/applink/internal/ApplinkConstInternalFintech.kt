package com.tokopedia.applink.internal

import com.tokopedia.applink.internal.ApplinkConstInternal.INTERNAL_SCHEME

/**
 * This class is used to store deeplink "tokopedia-android-internal://marketplace".
 */
object ApplinkConstInternalFintech {

    const val HOST_FINTECH = "fintech"
    val INTERNAL_FINTECH = "${INTERNAL_SCHEME}://${HOST_FINTECH}"

}
