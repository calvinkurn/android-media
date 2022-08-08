package com.tokopedia.feedcomponent.data.pojo

import com.google.gson.annotations.SerializedName

data class FeedXTrackViewerResponse(
    @SerializedName("success")
    val success: Boolean = false)
{
    data class Response(
        @SerializedName("feedXTrackViewer")
        val feedXTrackViewerResponse: FeedXTrackViewerResponse
    )
}