package com.tokopedia.mvc.domain.entity.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PeriodType(val type: Int) : Parcelable {
    DAY(1),
    WEEK(2),
    MONTH(3)
}
