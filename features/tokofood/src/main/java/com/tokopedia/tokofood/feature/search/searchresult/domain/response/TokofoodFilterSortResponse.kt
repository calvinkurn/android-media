package com.tokopedia.tokofood.feature.search.searchresult.domain.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DataValue

data class TokofoodFilterSortResponse(
    @SerializedName("tokofoodFilterAndSort")
    val tokofoodFilterAndSort: DataValue = DataValue()
)