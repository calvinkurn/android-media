package com.tokopedia.filter.common.helper

fun String.toMapParam(): Map<String, String> {
    if (this.isEmpty()) return mapOf()

    return split("&")
            .associateTo(HashMap()) { it.createKeyValuePair() }
            .apply { remove("") }
}

private fun String.createKeyValuePair(): Pair<String, String> {
    if (!this.contains("=")) return Pair("", "")

    val (key, value) = split("=")
    return key to value
}