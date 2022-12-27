package com.tokopedia.mvc.presentation.bottomsheet.changequota.model

import android.os.Parcelable
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateQuotaModel(
    val voucherId: Long = 0,
    val voucherName: String = "",
    val isMultiPeriod: Boolean = false,
    var isApplyToAllPeriodCoupon: Boolean = false,
    val maxBenefit: Long = 0,
    val currentQuota: Long = 0,
    var isVoucherProduct: Boolean = false,
    var voucherStatus: VoucherStatus = VoucherStatus.NOT_STARTED,
    val voucherType: PromoType = PromoType.FREE_SHIPPING
) : Parcelable
