package com.tokopedia.feedcomponent.data.feedrevamp


import com.google.gson.annotations.SerializedName

data class FeedData(
    @SerializedName("feedXHome")
    var feedXHome: FeedXHome
)