package com.tokopedia.common_digital.common.presentation.model

import com.tokopedia.common_digital.atc.data.response.ErrorAtc

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
        val userId: String = "",
        val isSpecialProduct: Boolean = false,
        val channelId: String = "",
        val redirectUrl: String = "",
        val atcError: ErrorAtc? = null
)
