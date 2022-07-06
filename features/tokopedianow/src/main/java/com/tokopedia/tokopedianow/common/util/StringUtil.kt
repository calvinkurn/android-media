package com.tokopedia.tokopedianow.common.util

object StringUtil {
    private const val DEFAULT_ZERO_STRING = "0"

    fun String?.getOrDefaultZeroString() = if (isNullOrBlank()) {
        DEFAULT_ZERO_STRING
    } else {
        this
    }
}