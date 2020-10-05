package com.tokopedia.notifications.util

fun <T> ArrayList<T>.onlyOne(): Boolean {
    if (size == 1) return true
    return false
}