package com.tokopedia.common_category.model.filter

import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DynamicFilterModel

data class FilterResponse(
        @field:SerializedName("dynamicAttribute")
        val dynamicAttribute: DynamicFilterModel? = null
)
