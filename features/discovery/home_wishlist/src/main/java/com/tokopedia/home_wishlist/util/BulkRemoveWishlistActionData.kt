package com.tokopedia.home_wishlist.util

data class BulkRemoveWishlistActionData(
        val isSuccess: Boolean = false,
        val isPartiallyFailed: Boolean = false,
        val message: String = ""
)