package com.tokopedia.createpost.common.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXMediaTagging(
        @SerializedName("tagIndex")
        var tagIndex: Int,
        @SerializedName("posX")
        var posX: Float,
        @SerializedName("posY")
        var posY: Float,
        var X: Float? = 0f,
        var Y: Float? = 0f,
        var rawX: Float? = 0f,
        var rawY: Float? = 0f,
        var mediaIndex:Int,
        var pointerPosition: Float? = -1f
)
