package com.tokopedia.content.common.navigation.util

/**
 * Created By : Jonathan Darwin on February 15, 2023
 */
internal fun StringBuilder.appendQuery(key: String, value: Any) {
    append("$key=$value")
}

internal fun StringBuilder.appendDivider() {
    append(DIVIDER)
}

internal fun buildAppLink(baseAppLink: String, query: String): String {
    return "$baseAppLink?$query"
}

private const val DIVIDER = "&"
