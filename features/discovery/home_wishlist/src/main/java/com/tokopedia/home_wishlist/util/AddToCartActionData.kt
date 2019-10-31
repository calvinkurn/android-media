package com.tokopedia.home_wishlist.util

data class AddToCartActionData(
        val position: Int = -1,
        val productId: Int = -1,
        val cartId: Int = -1,
        val isSuccess: Boolean = false,
        val message: String = ""
)