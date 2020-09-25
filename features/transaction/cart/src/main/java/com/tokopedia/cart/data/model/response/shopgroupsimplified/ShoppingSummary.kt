package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ShoppingSummary(
        @SerializedName("total_wording")
        val totalWording: String = "",
        @SerializedName("total_value")
        val totalValue: Int = 0,
        @SerializedName("discount_total_wording")
        val discountTotalWording: String = "",
        @SerializedName("discount_value")
        val discountValue: Int = 0,
        @SerializedName("payment_total_wording")
        val paymentTotalWording: String = "",
        @SerializedName("promo_wording")
        val promoWording: String = "",
        @SerializedName("promo_value")
        val promoValue: Int = 0,
        @SerializedName("seller_cashback_wording")
        val sellerCashbackWording: String = "",
        @SerializedName("seller_cashback_value")
        val sellerCashbackValue: Int = 0
)