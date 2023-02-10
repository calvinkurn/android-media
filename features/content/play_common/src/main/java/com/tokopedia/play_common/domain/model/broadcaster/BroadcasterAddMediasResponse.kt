package com.tokopedia.play_common.domain.model.broadcaster

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on November 28, 2022
 */

data class BroadcasterAddMediasResponse(
    @SerializedName("broadcasterAddMedias")
    val wrapper: Wrapper = Wrapper(),
) {

    data class Wrapper(
        @SerializedName("mediaIDs")
        val mediaIDs: List<String> = emptyList(),
    )
}
