package com.tokopedia.troubleshooter.notification.util

fun <T> MutableList<T>.dropFirst() {
    if (isEmpty()) return
    removeAt(0)
}

fun Any?.isNotNull(): Boolean {
    return this != null
}

inline fun <T> Iterable<T>.getWithIndex(predicate: (T) -> Boolean): Pair<Int, T>? {
    forEachIndexed { index, element -> if (predicate(element)) return Pair(index, element) }
    return null
}