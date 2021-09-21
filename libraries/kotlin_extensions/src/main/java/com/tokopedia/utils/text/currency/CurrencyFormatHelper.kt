package com.tokopedia.utils.text.currency

import android.widget.EditText
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import java.lang.Integer.parseInt
import java.text.NumberFormat
import java.util.*

/**
 * use [CurrencyFormatUtil] for flexibility of currency (can use other than IDR, USD, etc)
 * modified by m.normansyah & steven.f
 * changed "," to "." for rupiah
 */
object CurrencyFormatHelper {
    private val RupiahFormat = NumberFormat.getIntegerInstance(Locale.US)
    private val DollarFormat = NumberFormat.getCurrencyInstance(Locale("en", "US"))
    // this flag intend to block textwatcher to be called recursively
    private var LockTextWatcher = false

    val RUPIAH = 1
    val OTHER = -1


    fun convertToRupiah(string: String): String {
        return try {
            val inrupiah: String = RupiahFormat.format(java.lang.Long.parseLong(string.replace("Rp", ""))).replace("$", "").replace("Rp", "").replace(".00", "").replace(",", ".")
            inrupiah
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            "Exception raised"
        }

    }

    fun convertToDollar(string: String): String {
        return try {
            val indollar: String = DollarFormat.format(java.lang.Double.parseDouble(string)).replace("$", "")
            indollar
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            "Exception raised"
        }

    }

    fun setToRupiah(et: EditText) {
        try {
            if (et.length() > 0 && !LockTextWatcher) {
                LockTextWatcher = true
                var tempCursorPos = et.selectionStart
                val tempLength = et.length()
                et.setText(RupiahFormat.format(java.lang.Long.parseLong(et.text.toString().replace("$", "").replace(".00", "").replace(".0", "").replace(",", "").replace(".", ""))).replace("$", "").replace(".00", "").replace(".0", ""))
                // Handler untuk tanda koma
                if (et.length() - tempLength == 1)
                // Untuk majuin cursor ketika nambah koma
                {
                    if (et.length() < 4)
                        tempCursorPos += 1
                    else if (et.text[tempCursorPos] != '.')
                    // Untuk mundur ketika mencoba menghapus koma
                        tempCursorPos += 1
                } else if (et.length() - tempLength == -1)
                // Mundurin cursor ketika hapus koma
                {
                    tempCursorPos -= 1
                } else if (et.length() > 3 && tempCursorPos < et.length() && tempCursorPos > 0)
                    if (et.text[tempCursorPos - 1] == '.') { // Mundurin cursor ketika menambah digit dibelakang koma
                        tempCursorPos -= 1
                    }

                // Set posisi cursor
                if (tempCursorPos < et.length() && tempCursorPos > -1)
                    et.setSelection(tempCursorPos)
                else if (tempCursorPos < 0)
                    et.setSelection(0)
                else
                    et.setSelection(et.length())
                LockTextWatcher = false
            }
        } catch (e: NumberFormatException) {
            LockTextWatcher = false
            e.printStackTrace()
        }

    }

    fun setToRupiahCheckPrefix(et: EditText) {
        try {
            val noFirstCharToIgnore = countPrefixCurrency(et.text.toString())
            if (et.length() > noFirstCharToIgnore && !LockTextWatcher) {
                LockTextWatcher = true
                var tempCursorPos = et.selectionStart
                val tempLength = et.length()
                val textToFormat = et.text.toString()
                val formattedText = RupiahFormat.format(parseInt(
                        textToFormat
                                .substring(noFirstCharToIgnore)
                                .replace("$", "")
                                .replace(",", "")
                                .replace(".", "")))
                        .replace("$", "")
                        .replace(",",".")
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
        var result = rupiah.replace("Rp", "")
        result = result.replace(",", "")
        result = result.replace(".", "")
        result = result.replace(" ", "")
        return result.toIntOrZero()
    }

    fun convertRupiahToLong(rupiah: String): Long {
        var result = rupiah.replace("Rp", "")
        result = result.replace(",", "")
        result = result.replace(".", "")
        result = result.replace(" ", "")
        return result.toLongOrZero()
    }
}