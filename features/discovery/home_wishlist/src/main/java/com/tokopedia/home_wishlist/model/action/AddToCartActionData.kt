package com.tokopedia.home_wishlist.model.action

import com.tokopedia.home_wishlist.model.entity.WishlistItem

data class AddToCartActionData(
        val position: Int = -1,
        val productId: Long = -1L,
        val cartId: String = "",
        val item: WishlistItem = WishlistItem(),
        override val message: String = "",
        override val isSuccess: Boolean = false
) : BaseActionData()