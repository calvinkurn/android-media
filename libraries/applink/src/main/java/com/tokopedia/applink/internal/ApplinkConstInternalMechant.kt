package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://merchant"
 */

object ApplinkConstInternalMechant {
    @JvmField
    val HOST_MERCHANT = "merchant"
    @JvmField
    val INTERNAL_MERCHANT = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_MERCHANT}"
    @JvmField
    val MERCHANT_REDIRECT_CREATE_SHOP = "${INTERNAL_MERCHANT}:/redirect-create-shop"
    @JvmField
    val MERCHANT_OPEN_CATALOG_PICKER = "${INTERNAL_MERCHANT}:/open-catalog-picker"
    @JvmField
    val MERCHANT_OPEN_PRODUCT_PREVIEW = "${INTERNAL_MERCHANT}:/open-product-preview"

    // Official Store Brandlist
    @JvmField
    val BRANDLIST = "${INTERNAL_MERCHANT}/official-store/brand/{category_id}/"
    // Official Store Brandlist - Search Page
    @JvmField
    val BRANDLIST_SEARCH = "${INTERNAL_MERCHANT}/official-store/brand-search"
}
