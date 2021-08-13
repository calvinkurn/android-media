package com.tokopedia.saldodetails.utils

import java.text.NumberFormat
import java.util.*

object CurrencyUtils {
    var locale = Locale("in", "ID")
    const val RUPIAH = "Rp"
    const val RUPIAH_FORMAT = "$RUPIAH %s"


    fun convertToCurrencyString(value: Long): String {
        return String.format(RUPIAH_FORMAT, NumberFormat.getNumberInstance(locale).format(value))
    }

    fun convertToCurrencyStringWithoutRp(value: Long): String {
        return NumberFormat.getNumberInstance(locale).format(value)
    }

    fun convertToCurrencyLongFromString(currency: String): Long {
        var currency = currency
        currency = currency.replace(RUPIAH,"")
        currency = currency.replace(".", "")
        return java.lang.Long.valueOf(currency.trim())
    }
}