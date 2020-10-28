package com.tokopedia.shop.common.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DynamicFilterModel

data class GqlDynamicFilterResponse(
    @SerializedName("filter_sort_product")
    @Expose
    val dynamicFilterModel : DynamicFilterModel = DynamicFilterModel()
)