package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia://hotel/dashboard"
 */

object ApplinkConstInternalMechant {

    @JvmField
    val HOST_MERCHANT = "merchant"
    @JvmField
    val INTERNAL_MERCHANT = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_MERCHANT}"

    @JvmField
    val MERCHANT_REDIRECT_CREATE_SHOP = "${INTERNAL_MERCHANT}:/redirect-create-shop"

}
