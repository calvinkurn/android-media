package com.tokopedia.search.result.product.wishlist

interface Wishlistable {

    fun setWishlist(productID: String, isWishlisted: Boolean)

    val isWishlisted: Boolean
}
