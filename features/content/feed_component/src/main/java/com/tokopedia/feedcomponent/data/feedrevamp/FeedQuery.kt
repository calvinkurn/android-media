package com.tokopedia.feedcomponent.data.feedrevamp


import com.google.gson.annotations.SerializedName

data class FeedQuery(
    @SerializedName("data")
    var feedData: FeedData
)