package com.tokopedia.catalog.model.raw

import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DynamicFilterModel

data class SearchFilterResponse(
        @field:SerializedName("filter_sort_product")
        val dynamicAttribute: DynamicFilterModel? = null
)
