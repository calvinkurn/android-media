package com.tokopedia.hotel.common.util

import android.text.TextUtils
import android.util.Patterns
import java.text.NumberFormat
import java.util.*

/**
 * @author by jessica on 20/04/20
 */

class HotelStringUtils {
    companion object {

        private val dotFormat = NumberFormat.getNumberInstance(Locale("in", "id"))
        private val commaFormat = NumberFormat.getNumberInstance(Locale("en", "US"))

        fun isValidEmail(contactEmail: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches() &&
                    !contactEmail.contains(".@") && !contactEmail.contains("@.")
        }

        fun convertPriceValue(price: Double, useCommaForThousand: Boolean): String? {
            return if (useCommaForThousand) commaFormat.format(price) else dotFormat.format(price)
        }

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
                    stringToReplace.replace("[^0-9.]".toRegex(), "")
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

        private fun omitNonNumeric(stringToReplace: String): String {
            return if (TextUtils.isEmpty(stringToReplace)) {
                "0"
            } else {
                stringToReplace.replace("[^\\d]".toRegex(), "")
            }
        }
    }
}

class ThousandString(var formattedString: String, var selection: Int)
