package com.tokopedia.promousage.domain.entity

data class BoAdditionalData(
    val promoId: Long = 0,
    val code: String = "",
    val cartStringGroup: String = "",
    val uniqueId: String = "",
    val shippingId: Long = 0,
    val shipperProductId: Long = 0,
    val benefitAmount: Double = 0.0,
    val shippingPrice: Double = 0.0,
    val shippingSubsidy: Long = 0,
    val benefitClass: String = "",
    val boCampaignId: Long = 0,
    val etaText: String = ""
)
