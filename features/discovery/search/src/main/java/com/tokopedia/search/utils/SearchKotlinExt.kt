package com.tokopedia.search.utils

fun <T> List<T>?.betweenFirstAndLast(): List<T> {
    if (this == null || this.size < 3) return listOf()

    return this.subList(1, this.size - 1)
}

fun <T> List<T>?.secondToLast(): List<T> {
    if (this == null || this.size < 2) return listOf()

    return this.subList(1, this.size)
}

fun Map<String, Any>?.convertValuesToString(): Map<String, String> {
    if (this == null) return mapOf()

    val mapValuesInString = mutableMapOf<String, String>()

    this.forEach { originalMap ->
        mapValuesInString[originalMap.key] = originalMap.value.toString()
    }

    return mapValuesInString
}

inline fun <reified T> List<Any>?.exists(): Boolean {
    if (this == null) return false

    this.forEach {
        if (it is T) return true
    }

    return false
}