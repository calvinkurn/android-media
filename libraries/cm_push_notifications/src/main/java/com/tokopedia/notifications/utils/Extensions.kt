package com.tokopedia.notifications.utils

fun <T> ArrayList<T>.onlyOne(): Boolean {
    if (size == 1) return true
    return false
}

fun <T, U> List<T>.intersect(
        uList: List<U>,
        filterPredicate : (T, U) -> Boolean
) = filter { m -> uList.any { filterPredicate(m, it)} }