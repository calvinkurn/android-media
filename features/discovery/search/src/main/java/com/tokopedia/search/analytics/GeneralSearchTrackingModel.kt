package com.tokopedia.search.analytics

data class GeneralSearchTrackingModel(
        val keyword: String = "",
        val treatment: String = "",
        val response: String = "",
        val isResultFound: Boolean = false,
        val category: Map<String, String> = mapOf()
)