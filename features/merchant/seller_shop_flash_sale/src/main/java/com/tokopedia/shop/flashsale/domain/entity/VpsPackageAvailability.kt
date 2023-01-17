package com.tokopedia.shop.flashsale.domain.entity

data class VpsPackageAvailability(
    val totalQuota: Int,
    val remainingQuota: Int,
    val isNearExpirePackageAvailable: Boolean,
    val packageNearExpire: Int
)