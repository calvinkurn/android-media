package com.tokopedia.mvc.presentation.list.model

import android.os.Parcelable
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherSource
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterModel (
    val keyword: String = "",
    val status: MutableList<VoucherStatus> = mutableListOf(VoucherStatus.NOT_STARTED, VoucherStatus.ONGOING, VoucherStatus.ENDED, VoucherStatus.STOPPED),
    val voucherType: MutableList<VoucherServiceType> = mutableListOf(VoucherServiceType.SHOP_VOUCHER, VoucherServiceType.PRODUCT_VOUCHER),
    val promoType: MutableList<PromoType> = mutableListOf(),
    val source: MutableList<VoucherSource> = mutableListOf(),
    val target: MutableList<VoucherTarget> = mutableListOf(),
    val targetBuyer: MutableList<VoucherTargetBuyer> = mutableListOf()
): Parcelable
