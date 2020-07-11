package com.tokopedia.troubleshooter.notification.util

fun <T> MutableList<T>.dropFirst() {
    if (isEmpty()) return
    removeAt(0)
}