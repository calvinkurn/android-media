package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXMediaTagging(
        @SerializedName("tagIndex")
        var tagIndex: Int,
        @SerializedName("posX")
        var posX: Float,
        @SerializedName("posY")
        var posY: Float,
)
