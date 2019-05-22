package com.tokopedia.search.result.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.data.DynamicFilterModel

data class GqlDynamicFilterResponse(
    @SerializedName("search_filter_product")
    @Expose
    val dynamicFilterModel : DynamicFilterModel = DynamicFilterModel()
)