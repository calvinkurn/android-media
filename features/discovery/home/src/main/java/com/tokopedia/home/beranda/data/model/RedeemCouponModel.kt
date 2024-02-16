package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.SerializedName

data class RedeemCouponModel(
    @SerializedName("hachikoRedeem")
    val hachikoRedeem: HachikoRedeem? = null
) {
    data class HachikoRedeem(
        @SerializedName("coupons")
        val coupons: List<Coupon> = listOf(),

        @SerializedName("redeemMessage")
        val redeemMessage: String = "",

        @SerializedName("ctaList")
        val ctaList: List<CtaAction> = listOf(),
    ) {
        data class Coupon(
            @SerializedName("cta")
            val cta: String = "",

            @SerializedName("code")
            val code: String = ""
        )

        data class CtaAction(
            @SerializedName("url")
            val url: String = "",

            @SerializedName("applink")
            val appLink: String = ""
        )

        fun redirectUrl(): String {
            if (ctaList.isEmpty()) return ""
            val cta = ctaList.first()

            return cta.url.ifEmpty { cta.appLink }
        }
    }
}
