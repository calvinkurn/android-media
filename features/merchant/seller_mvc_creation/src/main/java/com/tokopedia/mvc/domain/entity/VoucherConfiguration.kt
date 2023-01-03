package com.tokopedia.mvc.domain.entity

import android.os.Parcelable
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PeriodType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class VoucherConfiguration(
    val voucherId: Long = 0,
    val benefitIdr: Long = 0,
    val benefitMax: Long = 0,
    val benefitPercent: Int = 0,
    val benefitType: BenefitType = BenefitType.NOMINAL,
    val promoType: PromoType = PromoType.FREE_SHIPPING,
    val isVoucherProduct: Boolean = false,
    val minPurchase: Long = 0,
    val productIds: List<Long> = emptyList(),
    val targetBuyer: VoucherTargetBuyer = VoucherTargetBuyer.ALL_BUYER,
    val quota: Long = 0,
    val isVoucherPublic: Boolean = true,
    val voucherName: String = "",
    val voucherCodePrefix: String = "",
    val voucherCode: String = "",
    val startPeriod: Date = Date(),
    val endPeriod: Date = Date(),
    val isPeriod: Boolean = false,
    val periodType: Int = PeriodType.MONTH.type,
    val periodRepeat: Int = 1,
    val totalPeriod: Int = 1,
    val warehouseId: Long = 0
) : Parcelable
