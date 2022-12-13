package com.tokopedia.mvc.domain.entity

data class GenerateImageProperty(
    val platform: String = "",
    val isPublic: String = "",
    val voucherBenefitType: String = "",
    val voucherCashbackType: String = "",
    val voucherCashbackPercentage: String = "",
    val voucherNominalAmount: String = "",
    val voucherNominalSymbol: String = "",
    val voucherDiscountType: String = "",
    val voucherDiscountPercentage: String = "",
    val shopLogo: String = "",
    val shopName: String = "",
    val voucherCode: String = "",
    val voucherStartDate: String = "",
    val voucherEndDate: String = "",
    val productCount: String = "",
    val productImage1: String,
    val productImage2: String,
    val productImage3: String,
    val audienceTarget: String
)
