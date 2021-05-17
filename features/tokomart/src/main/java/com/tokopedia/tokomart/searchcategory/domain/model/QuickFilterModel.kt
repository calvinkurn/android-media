package com.tokopedia.tokomart.searchcategory.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DataValue

data class QuickFilterModel(
        @SerializedName("filter_sort_product")
        @Expose
        val filterSortProduct: FilterSortProduct = FilterSortProduct()
) {

    data class FilterSortProduct(
            @SerializedName("data")
            @Expose
            val data: DataValue = DataValue()
    )
}