package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class CatalogFilterBase(
    @SerializedName("categories")
    var categories: MutableList<CatalogCategory> = mutableListOf(),
    @SerializedName("sortType")
    var sortType: List<CatalogSortType> = listOf(),
    @SerializedName("pointRanges")
    var pointRanges: List<CatalogFilterPointRange> = listOf(),
)
