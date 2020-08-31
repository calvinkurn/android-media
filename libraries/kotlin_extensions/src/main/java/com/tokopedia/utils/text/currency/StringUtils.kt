package com.tokopedia.utils.text.currency

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

/**
 * Created by User on 11/10/2017.
 */

object StringUtils {

    fun convertListToStringDelimiter(list: List<String>, delimiter: String): String {
        val builder = StringBuilder()
        val it = list.iterator()
        while (it.hasNext()) {
            builder.append(it.next())
            if (it.hasNext()) {
                builder.append(delimiter)
            }
        }
        return builder.toString()
    }

    fun isNotBlank(shareUrl: String): Boolean {
        return !isBlank(shareUrl)
    }

    fun isBlank(shareUrl: String?): Boolean {
        return shareUrl == null || shareUrl.length == 0
    }

    fun isEmptyNumber(numberString: String?): Boolean {
        return numberString == null || numberString.length == 0 || "0" == numberString
    }

    fun containInList(stringList: List<String>?, stringToCheck: String): Boolean {
        if (stringList == null || TextUtils.isEmpty(stringToCheck)) {
            return false
        }
        var i = 0
        val sizei = stringList.size
        while (i < sizei) {
            if (stringToCheck == stringList[i]) {
                return true
            }
            i++
        }
        return false
    }

    fun omitPunctuationAndDoubleSpace(stringToReplace: String): String {
        return if (TextUtils.isEmpty(stringToReplace)) {
            ""
        } else {
            stringToReplace.replace("\\r|\\n".toRegex(), " ")
                    .replace("\\s+".toRegex(), " ")
                    .replace("[^0-9a-zA-Z ]".toRegex(), "")
                    .trim { it <= ' ' }
        }
    }

    fun omitNonNumeric(stringToReplace: String): String {
        return if (TextUtils.isEmpty(stringToReplace)) {
            "0"
        } else {
            stringToReplace.replace("[^\\d]".toRegex(), "")
        }
    }

    fun convertToNumeric(stringToReplace: String, useCommaInThousandSeparator: Boolean): Double {
        if (TextUtils.isEmpty(stringToReplace)) {
            return 0.0
        } else {
            val result: String
            if (useCommaInThousandSeparator) {
                result = stringToReplace.replace("[^0-9\\.]".toRegex(), "")
            } else {
                result = stringToReplace.replace("[^0-9,]".toRegex(), "").replaceFirst(",".toRegex(), ".")
            }
            try {
                return java.lang.Double.parseDouble(result)
            } catch (e: NumberFormatException) {
                return java.lang.Double.parseDouble(omitNonNumeric(result))
            }

        }
    }

    fun containNonSpaceAlphaNumeric(stringToCheck: String): Boolean {
        if (TextUtils.isEmpty(stringToCheck)) {
            return false
        } else {
            val p = Pattern.compile("[^a-zA-Z 0-9]")
            return p.matcher(stringToCheck).find()
        }
    }

    fun removeComma(numericString: String): String {
        return numericString.replace(",", "")
    }

    fun removePeriod(numericString: String): String {
        return numericString.replace(".", "")
    }

    fun isValidEmail(contactEmail: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches() &&
                !contactEmail.contains(".@") && !contactEmail.contains("@.")
    }
}