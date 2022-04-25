package com.tokopedia.createpost.producttag.view.uimodel

import androidx.annotation.IdRes
import com.tokopedia.createpost.createpost.R

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
data class ProductTagSource(
    val type: Type
) {
    enum class Type {
        GLOBAL_SEARCH, WISHLIST, SHOP, LAST_PURCHASE, UNKNOWN
    }

    companion object {
        private const val GLOBAL_SEARCH = "global_search"
        private const val WISHLIST = "wishlist"
        private const val OWN_SHOP = "own_shop"
        private const val LAST_PURCHASE = "last_purchase"

        fun mapFromString(s: String) = ProductTagSource(
            type = getSourceType(s),
        )

        private fun getSourceType(s: String): Type {
            return when(s) {
                GLOBAL_SEARCH -> Type.GLOBAL_SEARCH
                WISHLIST -> Type.WISHLIST
                OWN_SHOP -> Type.SHOP
                LAST_PURCHASE -> Type.LAST_PURCHASE
                else -> Type.UNKNOWN
            }
        }
    }
}