package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName
data class FeedXGQLResponse(

        @SerializedName("feedXGetActivityProducts")
        val data: FeedXGetActivityProductsResponse
        )

data class FeedXGetActivityProductsResponse(
        @SerializedName("products")
        var products: List<FeedXProduct> = emptyList(),
        @SerializedName("nextCursor")
        var nextCursor: String = "",
)
