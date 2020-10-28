package com.tokopedia.discovery2.discoveryext

fun <E> List<E>?.checkForNullAndSize(position: Int): List<E>? {
    if (!this.isNullOrEmpty() && this.size > position) {
        return this
    }
    return null
}