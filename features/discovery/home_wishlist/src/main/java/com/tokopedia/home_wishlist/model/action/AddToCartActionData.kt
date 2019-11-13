package com.tokopedia.home_wishlist.model.action

import com.tokopedia.home_wishlist.model.entity.WishlistItem

data class AddToCartActionData(
        val position: Int = -1,
        val productId: Int = -1,
        val cartId: Int = -1,
        val item: WishlistItem = WishlistItem(),
        override val message: String = "",
        override val isSuccess: Boolean = false
) : BaseActionData()