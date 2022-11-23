package com.tokopedia.mvc.presentation.list.model

import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherSubsidy
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer

data class FilterModel (
    val keyword: String = "",
    val status: MutableList<VoucherStatus> = mutableListOf(VoucherStatus.NOT_STARTED, VoucherStatus.ONGOING),
    val type: PromoType? = null,
    val voucherSubsidy: VoucherSubsidy = VoucherSubsidy.SELLER_AND_TOKOPEDIA,
    val target: VoucherTarget? = null,
    val targetBuyer: VoucherTargetBuyer? = null
)
