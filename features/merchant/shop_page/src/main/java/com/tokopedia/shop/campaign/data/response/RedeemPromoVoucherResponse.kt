package com.tokopedia.shop.campaign.data.response


import com.google.gson.annotations.SerializedName

data class RedeemPromoVoucherResponse(
    @SerializedName("hachikoRedeem")
    val hachikoRedeem: HachikoRedeem
) {
    data class HachikoRedeem(
        @SerializedName("coupons")
        val coupons: List<Coupon>,
        @SerializedName("redeemMessage")
        val redeemMessage: String
    ) {
        data class Coupon(
            @SerializedName("code")
            val code: String,
            @SerializedName("cta")
            val cta: String,
            @SerializedName("cta_desktop")
            val ctaDesktop: String,
            @SerializedName("description")
            val description: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("owner")
            val owner: Int,
            @SerializedName("promo_id")
            val promoId: Int,
            @SerializedName("title")
            val title: String
        )
    }
}
