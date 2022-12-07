package com.tokopedia.mvc.domain.entity

import android.os.Parcelable
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class VoucherInformation (
    val voucherName: String = "test",
    val code: String = "code test",
    val target: VoucherTarget = VoucherTarget.PUBLIC,
    val startPeriod: Date = Date(),
    val endPeriod: Date = Date(),
): Parcelable
