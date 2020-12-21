package com.tokopedia.homenav.common.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun convertPriceValueToIdrFormat(price: Long, hasSpace: Boolean): String? {
    val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
    kursIndonesia.maximumFractionDigits = 0
    val formatRp = DecimalFormatSymbols()
    formatRp.currencySymbol = "Rp" + if (hasSpace) " " else ""
    formatRp.groupingSeparator = '.'
    formatRp.monetaryDecimalSeparator = '.'
    formatRp.decimalSeparator = '.'
    kursIndonesia.decimalFormatSymbols = formatRp
    val result = kursIndonesia.format(price)
    return result.replace(",", ".")
}