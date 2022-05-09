package com.tokopedia.createpost.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
enum class ProductTagSource(val tag: String) {
    GlobalSearch("global_search"),
    Wishlist("wishlist"),
    MyShop("own_shop"),
    LastPurchase("last_purchase"),
    LastTagProduct("last_tag_product"),
    Unknown("");

    companion object {

        fun mapFromString(s: String): ProductTagSource {
            return when(s) {
                GlobalSearch.tag -> GlobalSearch
                Wishlist.tag -> Wishlist
                MyShop.tag -> MyShop
                LastTagProduct.tag -> LastTagProduct
                LastPurchase.tag -> LastPurchase
                else -> Unknown
            }
        }
    }
}