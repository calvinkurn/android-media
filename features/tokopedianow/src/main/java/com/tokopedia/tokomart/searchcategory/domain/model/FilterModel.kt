package com.tokopedia.tokomart.searchcategory.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel

data class FilterModel(
        @SerializedName("filter_sort_product")
        @Expose
        val filterSortProduct: DynamicFilterModel = DynamicFilterModel()
)
