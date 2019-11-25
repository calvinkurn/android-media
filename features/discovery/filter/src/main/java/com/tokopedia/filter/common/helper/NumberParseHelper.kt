package com.tokopedia.filter.common.helper

object NumberParseHelper {
    fun safeParseInt(text: String): Int {
        try {
            return Integer.parseInt(text)
        } catch (e: NumberFormatException) {
            return 0
        }

    }
}
