package com.tokopedia.shop.campaign.domain.entity

data class PromoVoucherDetail(
    val activePeriodDate: String,
    val buttonStr: String,
    val cta: String,
    val howToUse: String,
    val id: Long,
    val imageUrl: String,
    val imageUrlMobile: String,
    val isDisabled: Boolean,
    val isDisabledButton: Boolean,
    val minimumUsage: String,
    val quota: Int,
    val subTitle: String,
    val thumbnailUrl: String,
    val thumbnailUrlMobile: String,
    val title: String,
    val tnc: String,
    val isGift: Int
)
