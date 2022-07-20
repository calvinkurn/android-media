package com.tokopedia.vouchercreation.common.extension

private const val EMPTY_STRING = ""

fun ArrayList<String>.getIndexAtOrEmpty(index : Int) : String {
    return try {
        this[index]
    } catch(e: Exception) {
        EMPTY_STRING
    }
}