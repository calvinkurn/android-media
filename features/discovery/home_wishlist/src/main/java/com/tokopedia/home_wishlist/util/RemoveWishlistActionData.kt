package com.tokopedia.home_wishlist.util

data class RemoveWishlistActionData(
        val productId: Int = -1,
        val isSuccess: Boolean = false,
        val message: String = ""
)