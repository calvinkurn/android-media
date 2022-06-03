package com.tokopedia.search.utils

import com.tokopedia.filter.common.data.Option

fun List<Option>?.toDropdownQuickFilterEventLabel(): String {
    if (this == null || this.isEmpty()) return ""
    val optionHashMap: HashMap<String, String> = HashMap()

    this.filter { it.inputState.toBoolean() }.forEach {
        if (optionHashMap.containsKey(it.key)) {
            optionHashMap[it.key] += ",${it.value}"
        } else {
            optionHashMap[it.key] = it.value
        }
    }

    val optionList = optionHashMap.map {
        "${it.key}=${it.value}"
    }

    return optionList.joinToString("&")
}