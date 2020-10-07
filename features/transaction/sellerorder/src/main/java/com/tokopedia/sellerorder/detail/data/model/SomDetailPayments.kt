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
        val totalPurchaseProtectionFee: Int = 0,
        val totalPurchaseProtectionFeeText: String = "",
        val totalPurchaseProtectionQuantity: Int = 0,
        val totalReadinessInsuranceFee: Int = 0,
        val totalReadinessInsuranceFeeText: String = "",
        val totalReadinessInsuranceQuantity: Int = 0,
        val codFeeText: String = "",
        val totalPriceText: String = "")