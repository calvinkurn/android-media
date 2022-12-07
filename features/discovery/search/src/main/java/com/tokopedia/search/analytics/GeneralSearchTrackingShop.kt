package com.tokopedia.search.analytics

data class GeneralSearchTrackingShop(
    val eventLabel: String,
    val pageSource: String,
    val relatedKeyword: String,
    val searchFilter: String,
    val externalReference: String,
)
