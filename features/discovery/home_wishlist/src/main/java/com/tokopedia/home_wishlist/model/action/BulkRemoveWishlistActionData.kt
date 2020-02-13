package com.tokopedia.home_wishlist.model.action

data class BulkRemoveWishlistActionData(
        override val isSuccess: Boolean = false,
        override val message: String = "",
        val productIds: List<String> = listOf(),
        val isPartiallyFailed: Boolean = false
): BaseActionData()