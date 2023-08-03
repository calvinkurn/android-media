package com.tokopedia.shop.campaign.domain.entity

data class ShopCampaignRedeemPromoVoucherResult(
    val slug: String,
    val couponCode: String,
    val campaignId: String,
    val widgetId: String,
    val redeemResult: RedeemPromoVoucherResult
)
