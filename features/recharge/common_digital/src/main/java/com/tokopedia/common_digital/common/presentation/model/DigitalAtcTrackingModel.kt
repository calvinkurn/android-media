package com.tokopedia.common_digital.common.presentation.model

/**
 * @author by jessica on 26/02/21
 */

data class DigitalAtcTrackingModel(
        val cartId: String = "",
        val productId: String = "",
        val operatorName: String = "",
        val categoryId: String = "",
        val categoryName: String = "",
        val priceText: String = "",
        val pricePlain: Double = 0.0,
        val isInstantCheckout: Boolean = false,
        val source: Int = 0,
        val userId: String = ""
)