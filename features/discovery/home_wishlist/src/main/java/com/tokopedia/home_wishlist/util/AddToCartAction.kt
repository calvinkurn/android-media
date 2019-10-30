package com.tokopedia.home_wishlist.util

data class AddToCartAction(
        val position: Int,
        val productId: Int,
        val cartId: Int,
        val isSuccess: Boolean,
        val message: String
)