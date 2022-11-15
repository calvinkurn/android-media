package com.tokopedia.play.broadcaster.shorts.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on November 15, 2022
 */
data class BroadcasterAddMediasResponse(
    @SerializedName("BroadcasterAddMedias")
    val wrapper: Wrapper = Wrapper(),
) {

    data class Wrapper(
        @SerializedName("mediaIDs")
        val mediaIDs: List<String> = emptyList(),
    )
}
