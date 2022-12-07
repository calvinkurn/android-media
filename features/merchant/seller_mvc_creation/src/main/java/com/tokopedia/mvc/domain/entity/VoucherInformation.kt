package com.tokopedia.mvc.domain.entity

import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import java.util.*

data class VoucherInformation (
    val voucherName: String = "test",
    val code: String = "code test",
    val target: VoucherTarget = VoucherTarget.PUBLIC,
    val startPeriod: Date = Date(),
    val endPeriod: Date = Date(),
)
