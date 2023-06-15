package com.tokopedia.content.common.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 07/01/21.
 */
data class BroadcasterReportTrackViewerResponse(
        @SerializedName("success")
        val success: Boolean = false,
) {

    data class Response(
            @SerializedName("broadcasterReportTrackViewer")
            val broadcasterReportTrackViewer: BroadcasterReportTrackViewerResponse = BroadcasterReportTrackViewerResponse()
    )
}
