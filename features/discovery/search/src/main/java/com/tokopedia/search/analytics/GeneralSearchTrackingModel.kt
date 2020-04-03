package com.tokopedia.search.analytics

data class GeneralSearchTrackingModel(
        val eventLabel: String = "",
        val isResultFound: Boolean = false,
        val categoryMapping: Map<String, String> = mapOf(),
        val relatedKeyword: String = ""
)