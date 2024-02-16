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
            @SerializedName("jsonMetadata")
            val jsonMetadata: String = "",
        )

        fun jsonMetaData(): String = ctaList.first().jsonMetadata
    }
}

data class RedeemCouponRedirection(
    @SerializedName("url")
    val url: String,

    @SerializedName("app_link")
    val appLink: String,
)
