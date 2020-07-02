package com.tokopedia.youtube_common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PageInfo(
        @SerializedName("totalResults")
        @Expose
        var totalResults: Int = 0,
        @SerializedName("resultsPerPage")
        @Expose
        var resultsPerPage: Int = 0
)