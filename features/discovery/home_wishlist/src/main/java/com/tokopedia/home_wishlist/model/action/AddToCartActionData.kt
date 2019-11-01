package com.tokopedia.home_wishlist.model.action

data class AddToCartActionData(
        val position: Int = -1,
        val productId: Int = -1,
        val cartId: Int = -1,
        override val message: String = "",
        override val isSuccess: Boolean = false
) : BaseActionData()