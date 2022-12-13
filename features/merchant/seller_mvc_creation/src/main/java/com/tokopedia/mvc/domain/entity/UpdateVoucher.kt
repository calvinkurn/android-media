package com.tokopedia.mvc.domain.entity

import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
/**
 * This model is used to have the same model for image upload, convert your models,
 * voucher/ voucherDetail to this class to upload images
 * */
data class UpdateVoucher(
    val platform: String = "",
    val isPublic: Boolean = true,
    val voucherBenefitType: BenefitType = BenefitType.NOMINAL,
    val shopLogo: String = "",
    val shopName: String = "",
    val voucherCode: String = "",
    val voucherStartDate: String = "",
    val voucherEndDate: String = "",
    val productCount: Long = 0,
    val productImageUrls: List<String> = emptyList(),
    val audienceTarget: VoucherTargetBuyer = VoucherTargetBuyer.ALL_BUYER,

    val voucherName: String = "",
    val discountTypeFormatted: String = "",
    val discountAmt: Int = 0,
    val discountAmtMax: Int = 0,
    val voucherId: Long = 0,
    val type: Int = 0,
    val minimumAmt: Int = 0,
    val quota: Int = 0,
    val discountPercentage: Int = 0
)
