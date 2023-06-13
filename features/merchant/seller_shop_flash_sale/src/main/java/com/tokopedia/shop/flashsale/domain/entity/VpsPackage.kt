package com.tokopedia.shop.flashsale.domain.entity

data class VpsPackage(
    val remainingQuota: Int,
    val currentQuota: Int,
    val isDisabled: Boolean,
    val originalQuota: Int,
    val packageEndTime: Long,
    val packageId: String,
    val packageName: String,
    val packageStartTime: Long
)
