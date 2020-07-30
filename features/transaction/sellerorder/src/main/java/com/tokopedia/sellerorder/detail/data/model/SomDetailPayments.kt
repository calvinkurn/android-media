package com.tokopedia.sellerorder.detail.data.model

/**
 * Created by fwidjaja on 2019-10-07.
 */
data class SomDetailPayments (
        val productsPriceText: String = "",
        val totalProducts: Int = 0,
        val totalWeight: String = "",
        val shippingPriceText: String = "",
        val insurancePriceValue: Int = 0,
        val insurancePriceText: String = "",
        val additionalPriceValue: Int = 0,
        val additionalPriceText: String = "",
        val totalPriceText: String = "")