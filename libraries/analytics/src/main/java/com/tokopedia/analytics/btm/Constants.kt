package com.tokopedia.analytics.btm

import androidx.annotation.Keep

/**
 *  page definition
 */
@Keep
enum class Page(val str: String) {
    HOMEPAGE("a87943.b83792"),
    FEED("a87943.b3679"),
    WISHLIST("a87943.b0751"),
    OFFICIAL_STORE("a87943.b9256"),
    ORDER_LIST("a87943.b1605"),
    TRANSAKSI("a87943.b0775"),
    PDP("a87943.b2815"),
    SKU("a87943.b8098"),
    CART("a87943.b2906"),
    CHECKOUT("a87943.b69823")
}

/**
 *  position definition
 */
@Keep
enum class Position(val str: String) {
    HOMEPAGE_BANNERS_BANNER("a87943.b83792.c0652.d4396")
}
