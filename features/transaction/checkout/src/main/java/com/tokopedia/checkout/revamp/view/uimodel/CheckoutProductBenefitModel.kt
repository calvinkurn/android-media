package com.tokopedia.checkout.revamp.view.uimodel

data class CheckoutProductBenefitModel(
    override val cartStringGroup: String,

    val productId: String = "",
    val productName: String = "",
    val imageUrl: String = "",
    val quantity: Int = 0,
    val originalPrice: Double = 0.0,
    val finalPrice: Double = 0.0,
    val weight: Int = 0,
    val weightActual: Int = 0,

    val headerText: String = "",
    val shouldShowHeader: Boolean = false
) : CheckoutItem
