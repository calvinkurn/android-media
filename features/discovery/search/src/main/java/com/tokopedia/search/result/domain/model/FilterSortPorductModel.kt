package com.tokopedia.search.result.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DataValue

data class FilterSortPorductModel(
    @SerializedName("filter_sort_product")
    @Expose
    val filterSortProductModel: FilterSortPorductModelData = FilterSortPorductModelData()
)

data class FilterSortPorductModelData(
    @SerializedName("data")
    @Expose
    val data: DataValue = DataValue()
)

