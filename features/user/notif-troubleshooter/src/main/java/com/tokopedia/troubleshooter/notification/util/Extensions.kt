package com.tokopedia.troubleshooter.notification.util

import java.lang.Exception

fun <T> MutableList<T>.dropFirst() {
    if (isEmpty()) return
    removeAt(0)
}

fun Any?.isNotNull(): Boolean {
    return this != null
}

fun String.prefixToken(): String {
    return try {
        this.substring(this.length - 8)
    } catch (e: Exception) {
        ""
    }
}

inline fun <T> Iterable<T>.getWithIndex(predicate: (T) -> Boolean): Pair<Int, T>? {
    forEachIndexed { index, element -> if (predicate(element)) return Pair(index, element) }
    return null
}