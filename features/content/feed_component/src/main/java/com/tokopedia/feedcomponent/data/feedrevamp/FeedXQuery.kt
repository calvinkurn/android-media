package com.tokopedia.feedcomponent.data.feedrevamp


import com.google.gson.annotations.SerializedName

data class FeedXQuery(
    @SerializedName("data")
    var feedData: FeedXData
)