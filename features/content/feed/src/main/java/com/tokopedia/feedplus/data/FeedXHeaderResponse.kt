package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

data class FeedXHeaderResponse(
    @SerializedName("feedXHeader")
    val feedXHeaderData: FeedXHeader = FeedXHeader()
)
