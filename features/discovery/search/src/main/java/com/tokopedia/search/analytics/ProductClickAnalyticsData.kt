package com.tokopedia.search.analytics

data class ProductClickAnalyticsData(
    val isOrganicAds: Boolean,
    val topadsTag: Int,
    val filterSortParams: String,
    val componentId: String,
)