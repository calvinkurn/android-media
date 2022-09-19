package com.tokopedia.entertainment.pdp.common.util

import java.text.NumberFormat
import java.util.*

object CurrencyFormatter {
    private val localeID = Locale("in", "ID")
    private val rupiahFormat: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
    fun getRupiahFormat(x: Int): String {
        rupiahFormat.maximumFractionDigits = 0
        return rupiahFormat.format(x)
    }

    fun getRupiahFormat(x: Long): String {
        rupiahFormat.maximumFractionDigits = 0
        return rupiahFormat.format(x)
    }
}