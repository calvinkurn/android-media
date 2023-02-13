package com.tokopedia.mvc.domain.entity

data class VoucherCreationMetadata(
    val accessToken: String,
    val isEligible: Int,
    val maxProduct: Int,
    val prefixVoucherCode: String,
    val shopId: Long,
    val token: String,
    val userId: Long,
    val discountActive: Boolean
)
