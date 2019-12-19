package com.tokopedia.home_wishlist.model.action

data class RemoveWishlistActionData(
        val productId: Int = -1,
        override val isSuccess: Boolean = false,
        override val message: String = ""
): BaseActionData()