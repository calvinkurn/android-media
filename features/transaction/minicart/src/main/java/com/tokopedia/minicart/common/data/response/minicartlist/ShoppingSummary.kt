package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class ShoppingSummary(
    @SerializedName("total_wording")
    val totalWording: String = "",
    @SerializedName("total_value")
    val totalValue: Double = 0.0,
    @SerializedName("discount_total_wording")
    val discountTotalWording: String = "",
    @SerializedName("discount_value")
    val discountValue: Double = 0.0,
    @SerializedName("payment_total_wording")
    val paymentTotalWording: String = "",
    @SerializedName("payment_total_value")
    val paymentTotalValue: Double = 0.0,
    @SerializedName("seller_cashback_wording")
    val sellerCashbackWording: String = "",
    @SerializedName("seller_cashback_value")
    val sellerCashbackValue: Double = 0.0
)
