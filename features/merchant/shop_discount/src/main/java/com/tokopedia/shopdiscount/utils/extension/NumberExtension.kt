package com.tokopedia.shopdiscount.utils.extension

import com.tokopedia.shopdiscount.utils.formatter.NumberFormatter

fun Number.thousandFormattedWithoutCurrency(): String {
    return NumberFormatter.decimalFormatterThousand.format(this)
}