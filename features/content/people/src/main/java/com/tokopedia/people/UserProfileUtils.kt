package com.tokopedia.people

import com.tokopedia.kotlin.extensions.view.thousandFormatted
import java.math.RoundingMode

object UserProfileUtils {
    public fun getFormattedNumber(number: Long): String {
        return if (number >= 10000) {
            number.thousandFormatted(hasSpace = true, digit = 0, roundingMode = RoundingMode.DOWN)
        } else {
            number.thousandFormatted(hasSpace = true, digit = 1, roundingMode = RoundingMode.DOWN)
        }
    }
}