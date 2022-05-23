package com.tokopedia.createpost.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
enum class ProductTagSource(val tag: String, val labelAnalytic: String) {
    GlobalSearch("global_search", "tokopedia"),
    Wishlist("wishlist", "wishlist"),
    MyShop("own_shop", ""),
    LastPurchase("last_purchase", "terakhir dibeli"),
    LastTagProduct("last_tag_product", "tokopedia"),
    Shop("shop", "toko saya"),
    Unknown("", "");

    companion object {

        fun mapFromString(s: String): ProductTagSource {
            return values().firstOrNull {
                it.tag == s
            } ?: Unknown
        }
    }
}