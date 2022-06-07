package com.tokopedia.tokofood.common.util

import java.text.NumberFormat
import java.util.*

object CurrencyFormatter {
    fun formatToRupiahFormat(amount: Double): String {
        val localeID = Locale("in", "ID")
        val rupiahFormat: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        rupiahFormat.maximumFractionDigits = 0
        return rupiahFormat.format(amount)
    }
}