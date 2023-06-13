package com.tokopedia.search.result.product.wishlist

import android.content.Intent
import com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WISHLIST_PRODUCT_ID
import com.tokopedia.discovery.common.constants.SearchConstant.Wishlist.WISHLIST_STATUS_IS_WISHLIST
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.ViewUpdater
import javax.inject.Inject

@SearchScope
class WishlistHelper @Inject constructor(
    private val viewUpdater: ViewUpdater
) {

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val productID = data?.getString(WISHLIST_PRODUCT_ID) ?: ""
        val isWishlist = data?.getBoolean(WISHLIST_STATUS_IS_WISHLIST) ?: false

        updateWishlistStatus(productID, isWishlist)
    }

    private fun Intent.getString(key: String): String = extras?.getString(key) ?: ""

    private fun Intent.getBoolean(key: String): Boolean = extras?.getBoolean(key) ?: false

    fun updateWishlistStatus(productID: String, isWishlisted: Boolean) {
        viewUpdater.itemList
            ?.filterIsInstance<Wishlistable>()
            ?.forEach {
                it.setWishlist(productID, isWishlisted)
            }
    }
}
