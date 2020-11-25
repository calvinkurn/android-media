package com.tokopedia.notifcenter.util

inline fun <reified T> List<T>.isSingleItem(): Boolean {
    return this.size == 1
}