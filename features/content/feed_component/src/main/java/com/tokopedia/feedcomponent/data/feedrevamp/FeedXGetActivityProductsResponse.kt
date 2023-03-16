package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXGQLResponse(

    @SerializedName("feedXGetActivityProducts")
    val data: FeedXGetActivityProductsResponse
)

data class FeedXGetActivityProductsResponse(
    @SerializedName("products")
    val products: List<FeedXProduct> = emptyList(),
    @SerializedName("isFollowed")
    val isFollowed: Boolean,
    @SerializedName("contentType")
    val contentType: String,
    @SerializedName("campaign")
    val campaign: FeedXCampaign,
    @SerializedName("nextCursor")
    val nextCursor: String = "",
    @SerializedName("hasVoucher")
    var hasVoucher: Boolean = false
)
