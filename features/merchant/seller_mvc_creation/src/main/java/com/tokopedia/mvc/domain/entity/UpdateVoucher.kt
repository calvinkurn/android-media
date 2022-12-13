package com.tokopedia.mvc.domain.entity

import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import java.util.*
/**
 * This model is used to have the same model for image upload, convert your models,
 * voucher/ voucherDetail to this class to upload images
 * */
data class UpdateVoucher(
    val platform: String = "",
    val isPublic: Boolean = true,
    val voucherBenefitType: BenefitType = BenefitType.NOMINAL,
    val voucherCashbackType: String = "",
    val voucherCashbackPercentage: String = "",
    val voucherNominalAmount: Long = 0,
    val voucherNominalSymbol: String = "",
    val voucherDiscountType: String = "",
    val voucherDiscountPercentage: String = "",
    val shopLogo: String = "",
    val shopName: String = "",
    val voucherCode: String = "",
    val voucherStartTime: Date = Date(),
    val voucherEndTime: Date = Date(),
    val productCount: Long = 0,
    val productImageUrls: List<String> = emptyList(),
    val audienceTarget: VoucherTargetBuyer = VoucherTargetBuyer.ALL_BUYER,
    val voucherType: PromoType = PromoType.FREE_SHIPPING
)
