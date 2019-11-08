package com.tokopedia.similarsearch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class SimilarProductsImageSearch(
        @SerializedName("data")
        @Expose
        val data: Data = Data()
)