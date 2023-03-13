package com.tokopedia.search.result.mps.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DataValue

data class MPSQuickFilterModel(
    @SerializedName("quick_filter")
    @Expose
    val quickFilterModel: DataValue = DataValue()
)
