package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class ShoppingSummary(
        @SerializedName("discount_total_wording")
        val discountTotalWording: String = "",
        @SerializedName("discount_value")
        val discountValue: Long = 0L,
        @SerializedName("payment_total_value")
        val paymentTotalValue: Long = 0L,
        @SerializedName("payment_total_wording")
        val paymentTotalWording: String = "",
        @SerializedName("promo_value")
        val promoValue: Long = 0L,
        @SerializedName("promo_wording")
        val promoWording: String = "",
        @SerializedName("seller_cashback_value")
        val sellerCashbackValue: Long = 0L,
        @SerializedName("seller_cashback_wording")
        val sellerCashbackWording: String = "",
        @SerializedName("total_value")
        val totalValue: Long = 0L,
        @SerializedName("total_wording")
        val totalWording: String = ""
)