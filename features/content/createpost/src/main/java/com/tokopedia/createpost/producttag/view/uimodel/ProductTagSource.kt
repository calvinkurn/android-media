package com.tokopedia.createpost.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
sealed class ProductTagSource {
    object GlobalSearch: ProductTagSource()
    object Wishlist: ProductTagSource()
    object LastTagProduct: ProductTagSource()
    object LastPurchase: ProductTagSource()
    object MyShop: ProductTagSource()
    object Unknown: ProductTagSource()

    companion object {
        private const val GLOBAL_SEARCH = "global_search"
        private const val WISHLIST = "wishlist"
        private const val OWN_SHOP = "own_shop"
        private const val LAST_PURCHASE = "last_purchase"
        private const val LAST_TAG_PRODUCT = "last_tag_product"

        fun mapFromString(s: String): ProductTagSource {
            return when(s) {
                GLOBAL_SEARCH -> GlobalSearch
                WISHLIST -> Wishlist
                OWN_SHOP -> MyShop
                LAST_TAG_PRODUCT -> LastTagProduct
                LAST_PURCHASE -> LastPurchase
                else -> Unknown
            }
        }
    }
}