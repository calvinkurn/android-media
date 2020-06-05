package com.tokopedia.entertainment.pdp.adapter.viewholder

import java.text.NumberFormat
import java.util.*

object CurrencyFormatter {
    private val localeID = Locale("in", "ID")
    private val rupiahFormat: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
    fun getRupiahAllowZeroFormat(x: Long): String {
        rupiahFormat.maximumFractionDigits = 0
        return rupiahFormat.format(x)
    }
}