package com.tokopedia.mvc.domain.entity

data class VoucherCreationMetadata(
    val accessToken: String,
    val isEligible: Int,
    val maxProduct: Int,
    val prefixVoucherCode: String,
    val shopId: Int,
    val token: String,
    val userId: Int
)
