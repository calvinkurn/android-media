package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXMediaRatio (
        @SerializedName("width")
        var width: Int = 0,
        @SerializedName("height")
        var height: Int = 0,
        )
