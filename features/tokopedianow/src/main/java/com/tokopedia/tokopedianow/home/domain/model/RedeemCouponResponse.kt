package com.tokopedia.tokopedianow.home.domain.model

import com.google.gson.annotations.SerializedName

data class RedeemCouponResponse(
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
            @SerializedName("appLink")
            val appLink: String = "",
            @SerializedName("code")
            val code: String = ""
        )
    }
}
