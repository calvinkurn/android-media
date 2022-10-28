package com.tokopedia.shopdiscount.select.domain.entity

data class ShopBenefit(
    val isUseVps: Boolean,
    val benefits: List<Benefit>
) {
    data class Benefit(
        val expiredAt: String,
        val expiredAtUnix: Long,
        val maxQuota: Int,
        val packageId: String,
        val packageName: String,
        val remainingQuota: Int,
        val shopGrade: Int,
        val shopTier: Int
    )
}