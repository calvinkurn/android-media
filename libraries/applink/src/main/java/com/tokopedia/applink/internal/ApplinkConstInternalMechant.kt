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

    // Official Store Brandlist
    @JvmField
    val BRANDLIST = "${INTERNAL_MERCHANT}/official-store/brand/{category_id}/"
    // Official Store Brandlist - Search Page
    @JvmField
    val BRANDLIST_SEARCH = "${INTERNAL_MERCHANT}/official-store/brand-search"

}
