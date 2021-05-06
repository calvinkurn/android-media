package com.tokopedia.tokomart.search.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchModel(
        @SerializedName("total_data")
        @Expose
        val totalData: Int = 0,
)