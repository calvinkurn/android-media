package com.tokopedia.search.analytics

data class GeneralSearchTrackingModel(
        val eventLabel: String,
        val isResultFound: String,
        val categoryIdMapping: String,
        val categoryNameMapping: String,
        val relatedKeyword: String
)