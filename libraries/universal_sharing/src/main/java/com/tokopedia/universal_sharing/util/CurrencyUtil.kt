package com.tokopedia.universal_sharing.util

import java.text.NumberFormat
import java.util.*

object CurrencyUtil {
    private val localeID = Locale("in", "ID")
    private val rupiahFormat: NumberFormat = NumberFormat.getCurrencyInstance(localeID)

    fun Double.toRupiahFormat(): String {
        rupiahFormat.maximumFractionDigits = 0
        return rupiahFormat.format(this)
    }
}
