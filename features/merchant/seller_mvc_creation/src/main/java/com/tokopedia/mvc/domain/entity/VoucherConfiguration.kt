package com.tokopedia.mvc.domain.entity

import android.os.Parcelable
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class VoucherConfiguration(
    val voucherId: Long,
    val benefitIdr: Long = 0,
    val benefitMax: Long = 0,
    val benefitPercent: Int = 0,
    val benefitType : BenefitType = BenefitType.NOMINAL,
    val promoType: PromoType = PromoType.FREE_SHIPPING,
    val isVoucherProduct: Boolean = false,
    val minPurchase: Long = 0,
    val productIds: List<Long> = emptyList(),
    val targetBuyer: VoucherTargetBuyer = VoucherTargetBuyer.ALL_BUYER,
    val quota: Long = 0,
    val isVoucherPublic: Boolean = false,
    val voucherName: String = "",
    val code: String = "",
    val startPeriod: Date = Date(),
    val endPeriod: Date = Date()
) : Parcelable
