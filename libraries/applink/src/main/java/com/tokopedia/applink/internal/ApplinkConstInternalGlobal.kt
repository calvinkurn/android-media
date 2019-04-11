package com.tokopedia.applink.internal

import com.tokopedia.applink.internal.ApplinkConstInternal.INTERNAL_SCHEME

/**
 * This class is used to store deeplink "tokopedia-android-internal://global".
 */
object ApplinkConstInternalGlobal {

    @JvmField
    val HOST_GLOBAL = "global"

    @JvmField
    val INTERNAL_GLOBAL = "${INTERNAL_SCHEME}://${HOST_GLOBAL}"

    // WithdrawActivity
    // tokopedia-android-internal://global/withdraw
    @JvmField
    val WITHDRAW = "$INTERNAL_GLOBAL/withdraw"
}
