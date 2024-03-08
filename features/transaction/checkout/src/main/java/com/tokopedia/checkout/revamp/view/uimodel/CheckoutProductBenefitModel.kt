package com.tokopedia.checkout.revamp.view.uimodel

data class CheckoutProductBenefitModel(
    override val cartStringGroup: String,

    val offerId: Long = 0,
    val productId: String = "",
    val productName: String = "",
    val imageUrl: String = "",
    val quantity: Int = 0,
    val originalPrice: Double = 0.0,
    val finalPrice: Double = 0.0,
    val weight: Int = 0,
    val weightActual: Int = 0,
    val shopId: String = "",

    val headerText: String = "",
    val shouldShowHeader: Boolean = false,

    // analytics
    var hasTriggerImpression: Boolean = false,
    val sumOfCheckoutProductsQuantity: Int = 0,
    val sumOfBenefitProductsQuantity: Int = 0
) : CheckoutItem
