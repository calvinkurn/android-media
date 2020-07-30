package com.tokopedia.filter.common.helper

object NumberParseHelper {
    fun safeParseInt(text: String): Int {
        return try {
            Integer.parseInt(text)
        } catch (e: NumberFormatException) {
            0
        }

    }
}
