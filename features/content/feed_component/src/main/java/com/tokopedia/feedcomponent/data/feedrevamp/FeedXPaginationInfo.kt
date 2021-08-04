package com.tokopedia.feedcomponent.data.feedrevamp


import com.google.gson.annotations.SerializedName

data class FeedXPaginationInfo(
    @SerializedName("cursor")
    var cursor: String,
    @SerializedName("hasNext")
    var hasNext: Boolean,
    @SerializedName("totalData")
    var totalData: Int
)