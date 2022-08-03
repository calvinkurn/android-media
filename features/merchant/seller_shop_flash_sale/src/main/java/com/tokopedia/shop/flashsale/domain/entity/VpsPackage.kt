package com.tokopedia.shop.flashsale.domain.entity

data class VpsPackage(
    val currentQuota: Int,
    val isDisabled: Boolean ,
    val originalQuota: Int,
    val packageEndTime: String ,
    val packageId: String,
    val packageName: String,
    val packageStartTime: String
)