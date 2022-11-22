package com.tokopedia.mvc.presentation.list.model

import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.entity.enums.VoucherSubsidy
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer

data class FilterModel (
    var keyword: String = "",
    var status: MutableList<VoucherStatus> = mutableListOf(VoucherStatus.NOT_STARTED, VoucherStatus.ONGOING),
    var type: PromoType? = null,
    var voucherSubsidy: VoucherSubsidy = VoucherSubsidy.SELLER_AND_TOKOPEDIA,
    var target: VoucherTarget? = null,
    var targetBuyer: VoucherTargetBuyer? = null
)
