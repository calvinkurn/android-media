package com.tokopedia.common_sdk_affiliate_toko.utils

internal object AffiliateSdkConstant {
    const val PDP = "0"
    const val SHOP = "1"
    const val CAMPAIGN = "3"
    const val PRODUCT = "Product"
    const val PAGE = "Page"
    const val PLATFORM = "android"
    const val AFFILIATE_UUID = "aff_unique_id"
    const val AFFILIATE_TRACKER_ID = "aff_tracker_id"
    const val WISHLIST = "4"
}

enum class AffiliateAtcSource(val source: String) {
    PDP(""),
    SHOP_PAGE("shop_page"),
    DISCOVERY_PAGE("discovery_page"),
    WISHLIST("wishlist")
}
