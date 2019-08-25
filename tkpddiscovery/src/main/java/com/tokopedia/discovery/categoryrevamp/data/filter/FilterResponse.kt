package com.tokopedia.discovery.categoryrevamp.data.filter

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.data.DynamicFilterModel

data class FilterResponse(
        @field:SerializedName("dynamicAttribute")
        val dynamicAttribute: DynamicFilterModel? = null
)
