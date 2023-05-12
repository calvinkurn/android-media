package com.tokopedia.shop.campaign.domain.entity

data class ExclusiveLaunchVoucher(
    val id: Long,
    val benefit: Long,
    val benefitMax: Long,
    val minimumPurchase: Long,
    val remainingQuota: Int,
    val isClaimed: Boolean
)
