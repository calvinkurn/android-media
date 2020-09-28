package com.tokopedia.search.analytics

data class GeneralSearchTrackingModel(
        val eventCategory: String,
        val eventLabel: String,
        val userId: String,
        val businessUnit: String,
        val isResultFound: String,
        val categoryIdMapping: String,
        val categoryNameMapping: String,
        val relatedKeyword: String
)