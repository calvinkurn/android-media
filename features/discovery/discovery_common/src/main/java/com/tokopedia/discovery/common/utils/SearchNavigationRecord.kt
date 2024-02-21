package com.tokopedia.discovery.common.utils

object SearchNavigationRecord {

    private val mutablePageList: MutableMap<String, Map<String, String>> = mutableMapOf()

    val pageList: Map<String, Map<String, String>>
        get() = mutablePageList

    fun containsPageKey(pageKeyPrefix: String) =
        pageList.keys.any { it.startsWith(pageKeyPrefix, ignoreCase = true) }

    internal fun addPage(key: String, data: Map<String, String> = mapOf()) {
        mutablePageList[key] = data
    }

    internal fun updateData(key: String, pageData: Map<String, String>) {
        mutablePageList[key] = mutablePageList[key]?.plus(pageData) ?: pageData
    }

    internal fun removePage(key: String) {
        mutablePageList.remove(key)
    }
}
