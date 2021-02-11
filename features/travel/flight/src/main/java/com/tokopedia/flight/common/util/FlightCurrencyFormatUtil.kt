package com.tokopedia.flight.common.util

import android.text.TextUtils
import com.tokopedia.utils.text.currency.StringUtils.omitNonNumeric
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

/**
 * @author by jessica on 2019-10-17
 *
 * improvement from CurrencyFormatUtil.java
 * Currently used for flight feature to format prices to rupiah format only
 */

class FlightCurrencyFormatUtil {

    companion object {

        fun convertToIdrPrice(price: Int, withSpace: Boolean = true): String {
            val kursIndonesia = DecimalFormat.getCurrencyInstance(Locale("in", "ID")) as DecimalFormat
            kursIndonesia.maximumFractionDigits = 0
            val formatRp = DecimalFormatSymbols()

            if (withSpace) {
                formatRp.currencySymbol = "Rp "
            } else {
                formatRp.currencySymbol = "Rp"
            }
            formatRp.groupingSeparator = '.'
            formatRp.monetaryDecimalSeparator = '.'
            formatRp.decimalSeparator = '.'
            kursIndonesia.decimalFormatSymbols = formatRp
            val result = kursIndonesia.format(price.toLong())

            return result.replace(",", ".")
        }

        fun convertToIdrPriceWithoutSymbol(price: Int): String {
            val kursIndonesia = DecimalFormat.getCurrencyInstance(Locale("in", "ID")) as DecimalFormat
            kursIndonesia.maximumFractionDigits = 0
            val formatRp = DecimalFormatSymbols()

            formatRp.currencySymbol = ""
            formatRp.groupingSeparator = '.'
            formatRp.monetaryDecimalSeparator = '.'
            formatRp.decimalSeparator = '.'
            kursIndonesia.decimalFormatSymbols = formatRp
            val result = kursIndonesia.format(price.toLong())

            return result.replace(",", ".")
        }

        private val dotFormat = NumberFormat.getNumberInstance(Locale("in", "id"))
        private val commaFormat = NumberFormat.getNumberInstance(Locale("en", "US"))

        fun getThousandSeparatorString(valueToFormat: Double, useComma: Boolean, selectionStart: Int): ThousandString? {
            val textToFormat = valueToFormat.toString()
            var formattedString = textToFormat
            var cursorEnd = selectionStart
            var separatorString = ','
            val sourceLength = textToFormat.length
            try {
                if (sourceLength > 0) {
                    if (useComma) {
                        formattedString = commaFormat.format(valueToFormat)
                        separatorString = ','
                    } else {
                        formattedString = dotFormat.format(valueToFormat)
                        separatorString = '.'
                    }
                    // same with before, just return as is.
                    val resultLength = formattedString.length
                    if (textToFormat == formattedString) {
                        return ThousandString(textToFormat, resultLength)
                    }
                    var tempCursorPos = selectionStart

                    // Handler untuk tanda koma
                    if (resultLength - selectionStart == 1) {
                        // Untuk majuin cursor ketika nambah koma
                        if (formattedString.length < 4) {
                            tempCursorPos += 1
                        } else if (formattedString[tempCursorPos] != separatorString) {
                            // Untuk mundur ketika mencoba menghapus koma
                            tempCursorPos += 1
                        }
                    } else if (resultLength - selectionStart == -1) {
                        // Mundurin cursor ketika hapus koma
                        tempCursorPos -= 1
                    } else if (resultLength > 3 && selectionStart < resultLength && selectionStart > 0) {
                        if (formattedString[selectionStart - 1] == separatorString) {
                            // Mundurin cursor ketika menambah digit dibelakang koma
                            tempCursorPos -= 1
                        }
                    } else {
                        tempCursorPos = resultLength
                    }

                    // Set posisi cursor
                    cursorEnd = if (tempCursorPos < resultLength && tempCursorPos > -1) {
                        tempCursorPos
                    } else if (tempCursorPos < 0) {
                        0
                    } else {
                        resultLength
                    }
                    return ThousandString(formattedString, cursorEnd)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return ThousandString(textToFormat, selectionStart)
            }
            return ThousandString(textToFormat, selectionStart)
        }

        fun convertToNumeric(stringToReplace: String, useCommaInThousandSeparator: Boolean): Double? {
            return if (TextUtils.isEmpty(stringToReplace)) {
                0.0
            } else {
                val result: String = if (useCommaInThousandSeparator) {
                    stringToReplace.replace("[^0-9\\.]".toRegex(), "")
                } else {
                    stringToReplace.replace("[^0-9,]".toRegex(), "").replaceFirst(",".toRegex(), ".")
                }
                try {
                    result.toDouble()
                } catch (e: NumberFormatException) {
                    omitNonNumeric(result).toDouble()
                }
            }
        }

        fun convertPriceValueToIdrFormatNoSpace(price: Int): String {
            return convertPriceValueToIdrFormat(price, false)
        }

        private fun convertPriceValueToIdrFormat(price: Int, hasSpace: Boolean): String {
            val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
            kursIndonesia.maximumFractionDigits = 0
            val formatRp = DecimalFormatSymbols()
            formatRp.currencySymbol = "Rp" + if (hasSpace) " " else ""
            formatRp.groupingSeparator = '.'
            formatRp.monetaryDecimalSeparator = '.'
            formatRp.decimalSeparator = '.'
            kursIndonesia.decimalFormatSymbols = formatRp
            val result = kursIndonesia.format(price.toLong())
            return result.replace(",", ".")
        }
    }


}

class ThousandString(var formattedString: String, var selection: Int)