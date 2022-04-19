package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://testapp".
 */
object ApplinkConstInternalTestApp {

    const val HOST_TESTAPP = "testapp"

    const val INTERNAL_TESTAPP = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_TESTAPP}"

    // TestApp LoginActivity
    // tokopedia-android-internal://testapp/login
    const val LOGIN = "$INTERNAL_TESTAPP/login"
}
