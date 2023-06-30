package com.tokopedia.search.result.mps.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel

data class MPSQuickFilterModel(
    @SerializedName("filter_sort_product")
    @Expose
    val quickFilterModel: DynamicFilterModel = DynamicFilterModel(),
)
