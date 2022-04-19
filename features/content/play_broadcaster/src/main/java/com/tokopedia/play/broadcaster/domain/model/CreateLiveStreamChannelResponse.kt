package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 05/06/20.
 */
data class CreateLiveStreamChannelResponse(
        @SerializedName("broadcasterCreateLivestream")
        val media: GetMedia
) {
    data class GetMedia(
            @SerializedName("mediaID")
            val mediaId: String,
            @SerializedName("livestreamID")
            val liveStreamId: String,
            @SerializedName("ingestURL")
            val ingestUrl: String,
            @SerializedName("streamURL")
            val streamUrl: String
    )
}