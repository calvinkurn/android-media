package com.tokopedia.saldodetails.commom.utils

import java.text.NumberFormat
import java.util.*

object CurrencyUtils {
    var locale = Locale("in", "ID")
    private const val RUPIAH = "Rp"
    private const val RUPIAH_FORMAT = "$RUPIAH %s"


    fun convertToCurrencyString(value: Long) =
        String.format(RUPIAH_FORMAT, NumberFormat.getNumberInstance(locale).format(value))

    fun convertToCurrencyLongFromString(currency: String): Long {
        var currency = currency
        currency = currency.replace(RUPIAH,"")
        currency = currency.replace(".", "")
        return currency.trim().toLong()
    }
}