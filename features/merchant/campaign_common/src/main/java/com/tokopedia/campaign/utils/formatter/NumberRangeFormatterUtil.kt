package com.tokopedia.campaign.utils.formatter

import com.tokopedia.kotlin.extensions.view.isZero

object NumberRangeFormatterUtil {
    fun getFormattedRangeString(
        min: Long,
        max: Long,
        formatNonRange: (min: Long) -> String,
        formatWithRange: (min: Long, max: Long) -> String
    ): String {
        return if (min.isZero() && max.isZero()) {
            ""
        } else if (min == max) {
            formatNonRange(min)
        } else if (min.isZero()) {
            formatNonRange(max)
        } else if (max.isZero()) {
            formatNonRange(min)
        } else {
            formatWithRange(min, max)
        }
    }
}