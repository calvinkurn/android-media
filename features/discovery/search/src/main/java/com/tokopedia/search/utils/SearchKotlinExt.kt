package com.tokopedia.search.utils

fun Map<String, Any>?.convertValuesToString(): Map<String, String> {
    if (this == null) return mapOf()

    val mapValuesInString = mutableMapOf<String, String>()

    this.forEach { originalMap ->
        mapValuesInString[originalMap.key] = originalMap.value.toString()
    }

    return mapValuesInString
}