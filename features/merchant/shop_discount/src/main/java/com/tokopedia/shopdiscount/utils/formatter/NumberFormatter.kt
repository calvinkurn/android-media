package com.tokopedia.shopdiscount.utils.formatter

import com.tokopedia.shopdiscount.utils.constant.LocaleConstant
import java.text.DecimalFormat
import java.text.NumberFormat

object NumberFormatter {
    private const val NUMBER_PATTERN = "#,###,###"
    val decimalFormatterThousand =
        (NumberFormat.getInstance(LocaleConstant.INDONESIA) as DecimalFormat).apply {
            applyPattern(NUMBER_PATTERN)
        }
}