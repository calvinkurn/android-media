package com.tokopedia.tokopedianow.common.util

object QueryParamUtil {

    private const val AMP = "&"
    private const val EQUAL = "="

    fun String.getStringValue(key: String): String {
        return Regex("$key=([^&]*)").find(this)?.groupValues?.lastOrNull().orEmpty()
    }

    fun String.getBooleanValue(key: String): Boolean {
        return Regex("$key=([^&]*)").find(this)?.groupValues?.lastOrNull().toBoolean()
    }

    fun String.mapToQueryParamsMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        split(AMP).forEach {
            val query = it.split(EQUAL)
            val key = query.first()
            val value = query.last()
            map[key] = value
        }
        return map
    }
}
