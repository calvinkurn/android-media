package com.tokopedia.tokopedianow.home.analytic

data class HomeAddToCartTracker(
    val position: Int,
    val quantity: Int,
    val cartId: String,
    val data: Any?
)
