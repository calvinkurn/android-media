package com.tokopedia.mvc.presentation.list.model

import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherSource
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.domain.entity.enums.VoucherType

data class FilterModel (
    val keyword: String = "",
    val status: MutableList<VoucherStatus> = mutableListOf(VoucherStatus.NOT_STARTED, VoucherStatus.ONGOING),
    val voucherType: MutableList<VoucherType> = mutableListOf(VoucherType.SHOP, VoucherType.PRODUCT),
    val promoType: MutableList<PromoType> = mutableListOf(),
    val source: MutableList<VoucherSource> = mutableListOf(),
    val target: MutableList<VoucherTarget> = mutableListOf(),
    val targetBuyer: MutableList<VoucherTargetBuyer> = mutableListOf()
)
