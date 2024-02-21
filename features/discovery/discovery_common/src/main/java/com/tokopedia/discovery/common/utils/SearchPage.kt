package com.tokopedia.discovery.common.utils

import java.util.*

const val SRP = "SRP"
const val AUTO_COMPLETE = "AUTO_COMPLETE"

fun generateSearchPageKey(prefix: String) = prefix + "_" + UUID.randomUUID().toString()

interface SearchPage {
    fun updateData(key: String, data: Map<String, String>)
}

class SearchPageImpl: SearchPage {

    override fun updateData(key: String, data: Map<String, String>) {
        SearchNavigationRecord.updateData(key, data)
    }
}
