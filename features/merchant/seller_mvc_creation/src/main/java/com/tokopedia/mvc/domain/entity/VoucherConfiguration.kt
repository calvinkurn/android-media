package com.tokopedia.mvc.domain.entity

import android.os.Parcelable
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import kotlinx.parcelize.Parcelize

@Parcelize
data class VoucherConfiguration(
    val benefitIdr: Long,
    val benefitMax: Long,
    val benefitPercent: Int,
    val benefitType : BenefitType,
    val promoType: PromoType,
    val isVoucherProduct: Boolean,
    val minPurchase: Long,
    val productIds: List<Long>,
    val targetBuyer: VoucherTargetBuyer
) : Parcelable
