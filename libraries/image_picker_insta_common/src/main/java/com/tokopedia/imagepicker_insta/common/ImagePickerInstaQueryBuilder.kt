package com.tokopedia.imagepicker_insta.common

/**
 * Created By : Jonathan Darwin on September 19, 2022
 */
object ImagePickerInstaQueryBuilder {

    private const val QUERY_SEPARATOR = "&"

    fun generateQuery(queries: List<Pair<String, Any>>): String {
        return buildString {
            queries.forEachIndexed { idx, e ->
                appendQuery(e.first, e.second)
                if(idx != queries.size-1) append(QUERY_SEPARATOR)
            }
        }
    }

    private fun StringBuilder.appendQuery(key: String, value: Any) {
        append("$key=$value")
    }
}
