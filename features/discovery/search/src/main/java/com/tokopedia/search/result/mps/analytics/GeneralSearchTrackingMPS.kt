package com.tokopedia.search.result.mps.analytics

data class GeneralSearchTrackingMPS(
    val eventLabel: String,
    val pageSource: String,
    val isResultFound: String,
    val userId: String,
    val relatedKeyword: String,
    val searchFilter: String,
    val externalReference: String,
    val componentId: String,
)
