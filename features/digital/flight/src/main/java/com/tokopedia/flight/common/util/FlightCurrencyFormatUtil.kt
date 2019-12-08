package com.tokopedia.flight.common.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/**
 * @author by jessica on 2019-10-17
 *
 * improvement from CurrencyFormatUtil.java
 * Currently used for flight feature to format prices to rupiah format only
 */

class FlightCurrencyFormatUtil {

    companion object {

        fun convertToIdrPrice(price: Int): String {
            val kursIndonesia = DecimalFormat.getCurrencyInstance(Locale("in", "ID")) as DecimalFormat
            kursIndonesia.maximumFractionDigits = 0
            val formatRp = DecimalFormatSymbols()

            formatRp.currencySymbol = "Rp "
            formatRp.groupingSeparator = '.'
            formatRp.monetaryDecimalSeparator = '.'
            formatRp.decimalSeparator = '.'
            kursIndonesia.decimalFormatSymbols = formatRp
            val result = kursIndonesia.format(price.toLong())

            return result.replace(",", ".")
        }
    }

}