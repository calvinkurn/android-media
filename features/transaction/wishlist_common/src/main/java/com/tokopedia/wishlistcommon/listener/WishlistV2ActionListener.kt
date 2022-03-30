package com.tokopedia.wishlistcommon.listener

interface WishlistV2ActionListener {
    fun onErrorAddWishList(throwable: Throwable, productId: String)
    fun onSuccessAddWishlist(productId: String)
    fun onErrorRemoveWishlist(throwable: Throwable, productId: String)
    fun onSuccessRemoveWishlist(productId: String)
}