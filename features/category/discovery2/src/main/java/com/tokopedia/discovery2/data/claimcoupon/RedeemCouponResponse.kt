package com.tokopedia.discovery2.data.claimcoupon

import com.google.gson.annotations.SerializedName

data class RedeemCouponResponse(
        @SerializedName("hachikoRedeem")
        val hachikoRedeem: HachikoRedeem? = null
) {
    data class HachikoRedeem(
            @SerializedName("coupons")
            val coupons: List<Coupon?>? = null,
            @SerializedName("reward_points")
            val rewardPoints: Int? = 0,
            @SerializedName("redeemMessage")
            val redeemMessage: String? = "",
    ) {
        data class Coupon(
                @SerializedName("code")
                val code: String? = "",
                @SerializedName("cta")
                val cta: String? = "",
                @SerializedName("cta_desktop")
                val ctaDesktop: String? = "",
                @SerializedName("description")
                val description: String? = "",
                @SerializedName("id")
                val id: Int? = 0,
                @SerializedName("owner")
                val owner: Int? = 0,
                @SerializedName("promo_id")
                val promoId: Int? = 0,
                @SerializedName("title")
                val title: String? = "",
                @SerializedName("appLink")
                val appLink: String? = "",
        )
    }
}