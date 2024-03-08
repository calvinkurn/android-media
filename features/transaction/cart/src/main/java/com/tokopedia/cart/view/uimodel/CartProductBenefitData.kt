package com.tokopedia.cart.view.uimodel

data class CartProductBenefitData(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val qty: Int = 0,
    val originalPrice: Double = 0.0,
    val finalPrice: Double = 0.0
)
