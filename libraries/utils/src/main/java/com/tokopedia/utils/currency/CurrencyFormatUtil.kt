package com.tokopedia.utils.currency

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

/**
 * move from tkpddesign
 * improvement from CurrencyFormatHelper
 * We can format the text without specifying the prefix (Rp/IDR/USD/etc/etc)
 *
 * This Util will return the ThousandString object, instead directly modified the edittext directly
 * to set the EditText, use the code like
 * CurrencyFormatUtil.ThousandString thousandString = CurrencyFormatUtil.getThousandSeparatorString(nonNumericString, false, editText.getSelectionStart());
 * editText.setText(thousandString.getFormattedString());
 * editText.setSelection(Math.min(editText.length(), thousandString.getSelection()));
 */
object CurrencyFormatUtil {
    private val dotFormat = NumberFormat.getNumberInstance(Locale("in", "id"))
    private val commaFormat = NumberFormat.getNumberInstance(Locale("en", "US"))
    /**
     * @param valueToFormat eg.14123.5
     * @param useComma e/g true
     * @param selectionStart eg. 0
     * @return 14,123.5 with selection start 0
     */
    fun getThousandSeparatorString(valueToFormat: Double, useComma: Boolean, selectionStart: Int): ThousandString {
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
                if (resultLength - selectionStart == 1) { // Untuk majuin cursor ketika nambah koma
                    if (formattedString.length < 4) {
                        tempCursorPos += 1
                    } else if (formattedString[tempCursorPos] != separatorString) { // Untuk mundur ketika mencoba menghapus koma
                        tempCursorPos += 1
                    }
                } else if (resultLength - selectionStart == -1) { // Mundurin cursor ketika hapus koma
                    tempCursorPos -= 1
                } else if (resultLength > 3 && selectionStart < resultLength && selectionStart > 0) {
                    if (formattedString[selectionStart - 1] == separatorString) { // Mundurin cursor ketika menambah digit dibelakang koma
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

    fun convertPriceValueToIdrFormat(price: Int, hasSpace: Boolean): String {
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

    fun convertPriceValueToIdrFormat(price: Long, hasSpace: Boolean): String {
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

    fun convertPriceValueToIdrFormat(price: Double, hasSpace: Boolean): String {
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

    fun convertPriceValue(price: Double, useCommaForThousand: Boolean): String {
        return if (useCommaForThousand) {
            commaFormat.format(price)
        } else {
            dotFormat.format(price)
        }
    }

    fun convertPriceValueToIdrFormatNoSpace(price: Int): String {
        return convertPriceValueToIdrFormat(price, false)
    }

    class ThousandString(var formattedString: String, var selection: Int)
}