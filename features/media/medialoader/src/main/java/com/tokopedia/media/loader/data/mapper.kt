package com.tokopedia.media.loader.data

fun List<Header>.getFailureType(): FailureType? {
    val key = "x-tkp-media-failure"

    val header = find { it.key() == key } ?: return null
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
