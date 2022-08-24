package com.tokopedia.utils.text.currency

import android.widget.EditText
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

/**
 * use [CurrencyFormatUtil] for flexibility of currency (can use other than IDR, USD, etc)
 * modified by m.normansyah & steven.f
 * changed "," to "." for rupiah
 */
object CurrencyFormatHelper {
    private val RupiahFormat = NumberFormat.getIntegerInstance(Locale.US)
    // this flag intend to block textwatcher to be called recursively
    private var LockTextWatcher = false

    val RUPIAH = 1
    val OTHER = -1

    private fun String.toRawStringOfNumbers(): String {
        return replace("Rp", "")
            .replace(",", "")
            .replace(".", "")
            .replace(" ", "")
    }

    fun convertToRupiah(string: String): String {
        return try {
            val inrupiah: String =
                RupiahFormat.format(
                    string
                        .replace("Rp", "")
                        .toLongOrZero()
                )
                    .replace("$", "")
                    .replace("Rp", "")
                    .replace(".00", "")
                    .replace(",", ".")
            inrupiah
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            "Exception raised"
        }

    }

    @Suppress("MagicNumber")
    fun setToRupiahCheckPrefix(et: EditText) {
        try {
            val noFirstCharToIgnore = countPrefixCurrency(et.text.toString())
            if (et.length() > noFirstCharToIgnore && !LockTextWatcher) {
                LockTextWatcher = true
                var tempCursorPos = et.selectionStart
                val tempLength = et.length()
                val textToFormat = et.text.toString()
                val formattedText = RupiahFormat.format(
                    textToFormat
                        .substring(noFirstCharToIgnore)
                        .replace("$", "")
                        .replace(",", "")
                        .replace(".", "")
                        .toIntOrZero()
                ).replace("$", "").replace(",", ".")
                et.setText(formattedText)
                if (et.length() - tempLength == 1) {
                    if (et.length() < 4 + noFirstCharToIgnore) {
                        tempCursorPos += 1
                    } else if (et.text[tempCursorPos] != '.') {
                        tempCursorPos += 1
                    }
                } else if (et.length() - tempLength == -1) {
                    tempCursorPos -= 1
                } else if (et.length() > 3 + noFirstCharToIgnore
                        && tempCursorPos < et.length()
                        && tempCursorPos > noFirstCharToIgnore) {
                    if (et.text[tempCursorPos - 1] == '.') {
                        tempCursorPos -= 1
                    }
                }
                if (tempCursorPos < et.length() && tempCursorPos > -1 + noFirstCharToIgnore)
                    et.setSelection(tempCursorPos)
                else if (tempCursorPos < noFirstCharToIgnore)
                    et.setSelection(noFirstCharToIgnore)
                else
                    et.setSelection(et.length())
                LockTextWatcher = false
            }
        } catch (e: Exception) {
            LockTextWatcher = false
            e.printStackTrace()
        }

    }

    fun countPrefixCurrency(string: String): Int {
        var count = 0
        var i = 0
        val sizei = string.length
        while (i < sizei) {
            val charString = string[i]
            if (Character.isDigit(charString)) {
                break
            } else {
                count++
            }
            i++
        }
        return count
    }

    /**
     * Use StringUtils instead
     * @param string
     * @return
     */
    @Deprecated("")
    fun removeNonNumeric(string: String): String {
        return string.replace(",", "")
    }

    fun removeCurrencyPrefix(string: String?): String? {
        if (string == null) return null
        var count = 0
        var i = 0
        val sizei = string.length
        while (i < sizei) {
            val charString = string[i]
            if (Character.isDigit(charString)) {
                break
            } else {
                count++
            }
            i++
        }
        return string.substring(count)
    }

    fun convertRupiahToInt(rupiah: String): Int {
        return rupiah.toRawStringOfNumbers().toIntOrZero()
    }

    fun convertRupiahToLong(rupiah: String): Long {
        return rupiah.toRawStringOfNumbers().toLongOrZero()
    }

    fun convertRupiahToBigDecimal(rupiah: String): BigDecimal {
        return rupiah.toRawStringOfNumbers().toBigDecimalOrNull() ?: BigDecimal.ZERO
    }
}