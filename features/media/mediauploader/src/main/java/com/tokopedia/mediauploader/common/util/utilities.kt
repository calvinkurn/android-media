package com.tokopedia.mediauploader.common.util

fun String.addPrefix(): String {
    val pattern = "[(<]".toRegex()
    val kodeError = "Kode Error:"

    if (!this.contains(pattern)) return this

    // get string index before < or (
    val requestIdIndex = this
        .indexOfFirst { it.toString().matches(pattern) }
        .takeIf { it > 0 } ?: this.length

    val message = this.substring(0, requestIdIndex).trim()
    val lastMessage = this.substring(requestIdIndex, this.length).trim()

    return "$message $kodeError $lastMessage"
}