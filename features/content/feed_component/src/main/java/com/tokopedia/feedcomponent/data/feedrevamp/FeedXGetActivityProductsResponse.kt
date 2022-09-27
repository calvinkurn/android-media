package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName
data class FeedXGQLResponse(

        @SerializedName("feedXGetActivityProducts")
        val data: FeedXGetActivityProductsResponse
        )

data class FeedXGetActivityProductsResponse(
        @SerializedName("products")
        var products: List<FeedXProduct> = emptyList(),
        @SerializedName("isFollowed")
        var isFollowed: Boolean ,
        @SerializedName("contentType")
        var contentType: String ,
        @SerializedName("campaign")
        var campaign: FeedXCampaign ,
        @SerializedName("nextCursor")
        var nextCursor: String = "",
)
