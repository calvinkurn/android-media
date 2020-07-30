package com.tokopedia.home_wishlist.model.action

data class ProductClickActionData(
        val position: Int = -1,
        val parentPosition: Int = -1,
        val productId: Int = -1
)