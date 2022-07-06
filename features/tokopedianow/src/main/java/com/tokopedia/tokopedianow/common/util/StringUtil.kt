package com.tokopedia.tokopedianow.common.util

object StringUtil {
    private const val DEFAULT_ZERO_STRING = "0"

    fun String.getOrDefaultZeroString() = this.ifBlank { DEFAULT_ZERO_STRING }
}