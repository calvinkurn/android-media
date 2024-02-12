package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.SerializedName

data class RedeemCouponModel(
    @SerializedName("hachikoRedeem")
    val hachikoRedeem: HachikoRedeem? = null
) {
    data class HachikoRedeem(
        @SerializedName("coupons")
        val coupons: List<Coupon> = listOf(),
        @SerializedName("redeemMessage")
        val redeemMessage: String = ""
    ) {
        data class Coupon(
            @SerializedName("cta")
            val cta: String = "",
            @SerializedName("code")
            val code: String = ""
        )
    }
}
