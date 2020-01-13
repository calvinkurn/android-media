package com.tokopedia.salam.umrah.common.util

import java.text.NumberFormat
import java.util.*
/**
 * @author by M on 1/11/19
 */
object CurrencyFormatter {
    private val localeID = Locale("in", "ID")
    private val rupiahFormat: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
    fun getRupiahFormat(x: Int): String {
        return rupiahFormat.format(x)
    }
}