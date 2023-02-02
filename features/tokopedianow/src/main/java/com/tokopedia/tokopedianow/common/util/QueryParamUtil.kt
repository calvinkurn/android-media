package com.tokopedia.tokopedianow.common.util

object QueryParamUtil {

    fun String.getStringValue(key: String): String {
        return Regex("$key=([^&]*)").find(this)?.groupValues?.lastOrNull().orEmpty()
    }

    fun String.getBooleanValue(key: String): Boolean {
        return Regex("$key=([^&]*)").find(this)?.groupValues?.lastOrNull().toBoolean()
    }
}
