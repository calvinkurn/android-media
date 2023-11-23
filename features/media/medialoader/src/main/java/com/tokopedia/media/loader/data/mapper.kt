package com.tokopedia.media.loader.data

fun failureTypeKey(): String {
    return "x-tkp-media-failure"
}

fun List<Header>.getFailureType(): FailureType? {
    val header = find { it.key() == failureTypeKey() } ?: return null
    if (header.values.isEmpty()) return null

    val values = header.firstValue()
    return FailureType.fromString(values)
}

fun Map<String, List<String>>.toModel(): List<Header> {
    return map {
        val (key, value) = it

        Header(
            key = key,
            values = value
        )
    }
}
