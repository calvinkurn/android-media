package com.tokopedia.content.common.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 05/02/21.
 */
data class TrackVisitChannelResponse(
        @SerializedName("success")
        val success: Boolean = false,
) {

    data class Response(
            @SerializedName("broadcasterReportVisitChannel")
            val model: TrackVisitChannelResponse
    )
}
