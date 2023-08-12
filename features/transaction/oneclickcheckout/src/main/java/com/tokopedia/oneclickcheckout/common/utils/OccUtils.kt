package com.tokopedia.oneclickcheckout.common.utils

import com.tokopedia.config.GlobalConfig

internal fun generateAppVersionForPayment(): String {
    return "android-${GlobalConfig.VERSION_NAME}"
}

operator fun <T> Collection<T>.plus(element: T): List<T> {
    val result = ArrayList<T>(size + 1)
    result.addAll(this)
    result.add(element)
    return result
}

fun <T> Collection<T>.removeAll(): List<T> {
    val result = ArrayList<T>(size + 1)
    result.clear()
    return result
}
