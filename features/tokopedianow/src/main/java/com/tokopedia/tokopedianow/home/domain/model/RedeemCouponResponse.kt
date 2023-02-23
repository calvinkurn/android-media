package com.tokopedia.tokopedianow.home.domain.model

data class RedeemCouponResponse(
    val data: Data
) {
    data class Data(
        val hachikoRedeem: HachikoRedeem
    ) {
        data class HachikoRedeem(
            val coupons: List<Coupon>,
            val redeemMessage: String,
            val reward_points: Int
        ) {
            data class Coupon(
                val appLink: String,
                val code: String,
                val cta: String,
                val cta_desktop: String,
                val description: String,
                val id: Int,
                val owner: Int,
                val promo_id: Int,
                val title: String,
                val url: String
            )
        }
    }
}
