package com.tokopedia.home_wishlist.view.ext

import com.tokopedia.home_wishlist.util.TypeAction
import com.tokopedia.home_wishlist.util.WishlistAction

fun WishlistAction.addToCart(callback: (Boolean, String) -> Unit) {
    if (typeAction == TypeAction.ADD_TO_CART) {
        callback.invoke(isSuccess, message)
    }
}
fun WishlistAction.bulkWishlist(callback: (Boolean, String) -> Unit) {
    if (typeAction == TypeAction.BULK_DELETE_WISHLIST) {
        callback.invoke(isSuccess, message)
    }
}
fun WishlistAction.addWishlist(callback: (Boolean, String) -> Unit) {
    if (typeAction == TypeAction.ADD_WISHLIST) {
        callback.invoke(isSuccess, message)
    }
}
fun WishlistAction.removeWishlist(callback: (Boolean, String) -> Unit) {
    if (typeAction == TypeAction.REMOVE_WISHLIST) {
        callback.invoke(isSuccess, message)
    }
}