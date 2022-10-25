package com.tokopedia.content.common.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
enum class ProductTagSource(val tag: String, val labelAnalytic: String) {
    GlobalSearch("global_search", "tokopedia"),
    Wishlist("wishlist", "wishlist"),
    MyShop("own_shop", "toko saya"),
    LastPurchase("last_purchase", "terakhir dibeli"),
    LastTagProduct("last_tag_product", "tokopedia"),
    Shop("shop", "toko"),
    Autocomplete("autocomplete", ""),
    Unknown("", "");

    companion object {

        fun mapFromString(s: String): ProductTagSource {
            return values().firstOrNull {
                it.tag == s
            } ?: Unknown
        }
    }
}