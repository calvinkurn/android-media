package com.tokopedia.search.analytics

data class GeneralSearchTrackingModel(
        val eventLabel: String,
        val isResultFound: Boolean,
        val categoryIdMapping: String,
        val categoryNameMapping: String,
        val relatedKeyword: String
)