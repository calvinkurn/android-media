package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ShoppingSummary(
    @SerializedName("total_wording")
    val totalWording: String = "",
    @SerializedName("discount_total_wording")
    val discountTotalWording: String = "",
    @SerializedName("payment_total_wording")
    val paymentTotalWording: String = "",
    @SerializedName("promo_wording")
    val promoWording: String = "",
    @SerializedName("seller_cashback_wording")
    val sellerCashbackWording: String = "",
    @SerializedName("add_ons")
    val summaryAddOnList: List<SummaryAddOn> = emptyList()
) {
    data class SummaryAddOn(
        @SerializedName("wording")
        val wording: String = "",
        @SerializedName("type")
        val type: Int = -1
    )
}
