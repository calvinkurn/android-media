package com.tokopedia.shop.campaign.domain.entity

data class RedeemPromoVoucherResult(
    val redeemMessage: String,
    val voucherCode: String,
    val promoId: Long
)
