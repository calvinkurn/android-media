package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 05/06/20.
 */
data class AddMediaChannelResponse(
        @SerializedName("broadcasterAddMedias")
        val mediaId: GetMediaId
) {
    data class AddMediaChannelData(

            @SerializedName("data")
            val data: AddMediaChannelResponse
    )
    data class GetMediaId(
            @SerializedName("mediaIDs")
            val mediaIds: List<String>
    )
}