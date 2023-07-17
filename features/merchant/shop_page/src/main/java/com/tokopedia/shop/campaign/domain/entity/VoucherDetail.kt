package com.tokopedia.shop.campaign.domain.entity

data class VoucherDetail(
    val activePeriodDate: String,
    val buttonLabel: String,
    val howToUse: String,
    val id: Long,
    val imageUrlMobile: String,
    val isDisabledButton: Boolean,
    val minimumUsage: String,
    val quota: Int,
    val title: String,
    val tnc: String,
    val isGift: Int,
    val voucherPrice: String,
    val expired: String
)
