package com.tokopedia.product.manage.feature.filter.data.model

import com.google.gson.annotations.SerializedName

data class ProductListMetaData(
        @SerializedName("tab")
        val tabs: List<Tab> = listOf(),
        @SerializedName("filter")
        val filters: List<Filter> = listOf(),
        @SerializedName("sort")
        val sorts: List<Sort> = listOf()
)