package com.tokopedia.productcard_compact.common.util

internal object NumberUtil {
    fun formatFloatToString(number: Float): String {
        val s = number.toString()
        return when {
            number == 0f -> ""
            s.endsWith(".0") -> {
                s.replace(".0", "")
            }
            else -> s
        }
    }
}
