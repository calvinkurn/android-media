package com.tokopedia.search.utils

import com.tokopedia.filter.common.data.Option

fun List<Option>?.joinActiveOptionsToString(): String {
    if (this == null || this.isEmpty()) return ""

    return this.filter { it.inputState.toBoolean() }
        .groupBy { it.key }
        .map { "${it.key}=${it.value.joinToString(separator = ","){ option -> option.value} }" }
        .joinToString(separator = "&")
}