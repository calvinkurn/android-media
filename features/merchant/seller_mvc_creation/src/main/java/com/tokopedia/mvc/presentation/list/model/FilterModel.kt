package com.tokopedia.mvc.presentation.list.model

import com.tokopedia.mvc.domain.entity.VoucherStatus
import com.tokopedia.mvc.domain.entity.VoucherSubsidy
import com.tokopedia.mvc.domain.entity.VoucherTarget
import com.tokopedia.mvc.domain.entity.VoucherTargetBuyer
import com.tokopedia.mvc.domain.entity.VoucherType

data class FilterModel (
    var keyword: String = "",
    var status: MutableList<VoucherStatus> = mutableListOf(VoucherStatus.NOT_STARTED, VoucherStatus.ONGOING),
    var type: VoucherType? = null,
    var voucherSubsidy: VoucherSubsidy = VoucherSubsidy.SELLER_AND_TOKOPEDIA,
    var voucherType: VoucherType? = null,
    var target: VoucherTarget? = null,
    var targetBuyer: VoucherTargetBuyer? = null
)
