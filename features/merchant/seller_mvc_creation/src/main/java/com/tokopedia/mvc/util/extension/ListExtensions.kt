package com.tokopedia.mvc.util.extension

private const val EMPTY_STRING = ""

fun List<String>.getIndexAtOrEmpty(index: Int): String {
    return try {
        this[index]
    } catch (e: Exception) {
        EMPTY_STRING
    }
}
