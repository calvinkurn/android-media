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

    // WebViewActivity (Web View in library)
    // Solution for sellerapp that does not have AppLinkWebsiteActivity
    // Activity can have title by putting "title=.."
    @JvmField
    val WEBVIEW = "${INTERNAL_GLOBAL}/webview?url={url}"
}
