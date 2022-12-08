package com.tokopedia.mvc.domain.entity

import android.os.Parcelable
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import kotlinx.parcelize.Parcelize

@Parcelize
data class VoucherConfiguration(
    val benefitIdr: Long = 0,
    val benefitMax: Long = 0,
    val benefitPercent: Int = 0,
    val benefitType: BenefitType = BenefitType.NOMINAL,
    val promoType: PromoType = PromoType.FREE_SHIPPING,
    val isVoucherProduct: Boolean = false,
    val minPurchase: Long = 0,
    val productIds: List<Long> = emptyList(),
    val targetBuyer: VoucherTargetBuyer = VoucherTargetBuyer.ALL_BUYER,
    val isVoucherPublic: Boolean = false,
    val voucherName: String = "",
    val voucherCodePrefix: String = "",
    val voucherCode: String = ""
) : Parcelable
