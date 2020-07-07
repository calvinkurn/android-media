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