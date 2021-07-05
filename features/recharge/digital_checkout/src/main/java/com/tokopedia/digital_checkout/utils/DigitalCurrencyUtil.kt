package com.tokopedia.digital_checkout.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

/**
 * @author by jessica on 14/01/21
 */

object DigitalCurrencyUtil {
    fun getStringIdrFormat(value: Double): String {
        val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
        kursIndonesia.maximumFractionDigits = 0
        val formatRp = DecimalFormatSymbols()
        formatRp.currencySymbol = "Rp"
        formatRp.groupingSeparator = '.'
        formatRp.monetaryDecimalSeparator = '.'
        formatRp.decimalSeparator = '.'
        kursIndonesia.decimalFormatSymbols = formatRp
        return kursIndonesia.format(value).replace(",", ".")
    }
}