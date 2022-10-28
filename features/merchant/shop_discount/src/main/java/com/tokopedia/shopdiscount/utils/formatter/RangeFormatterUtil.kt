package com.tokopedia.shopdiscount.utils.formatter

import com.tokopedia.kotlin.extensions.view.isZero

object RangeFormatterUtil {
    fun getFormattedRangeString(
        min: Int,
        max: Int,
        formatNonRange: (min: Int) -> String,
        formatWithRange: (min: Int, max: Int) -> String
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