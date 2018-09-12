package com.tokopedia.product.manage.item.video.domain.model.youtube

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