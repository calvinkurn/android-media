package com.tokopedia.wishlistcommon.listener

interface WishlistV2ActionListener {
    fun onErrorAddWishList(errorMessage: String?, productId: String?)
    fun onSuccessAddWishlist(productId: String?)
    fun onErrorRemoveWishlist(errorMessage: String?, productId: String?)
    fun onSuccessRemoveWishlist(productId: String?)
}