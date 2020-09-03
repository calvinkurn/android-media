package com.tokopedia.search.utils

import androidx.recyclerview.widget.RecyclerView

fun Map<String, Any>?.convertValuesToString(): Map<String, String> {
    if (this == null) return mapOf()

    val mapValuesInString = mutableMapOf<String, String>()

    this.forEach { originalMap ->
        mapValuesInString[originalMap.key] = originalMap.value.toString()
    }

    return mapValuesInString
}

internal fun RecyclerView.addItemDecorationIfNotExists(itemDecoration: RecyclerView.ItemDecoration) {
    val hasNoItemDecoration = itemDecorationCount == 0
    if (hasNoItemDecoration) addItemDecoration(itemDecoration)
}

internal fun String?.decodeQueryParameter(): String {
    this ?: return ""

    val queryParametersSplitIndex = this.indexOf("?")
    if (queryParametersSplitIndex < 0) return this

    val path = this.substring(0, queryParametersSplitIndex)
    val queryParameters = this.substring(queryParametersSplitIndex + 1, length)
    val queryParameterEncoded = UrlParamUtils.generateUrlParamString(UrlParamUtils.getParamMap(queryParameters))

    return "$path?$queryParameterEncoded"
}
