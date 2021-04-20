package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 05/02/21.
 */
data class VisitChannelTracking(
        @SerializedName("success")
        val success: Boolean = false,
) {

    data class Response(
            @SerializedName("broadcasterReportVisitChannel")
            val reportVisitChannelTracking: VisitChannelTracking
    )
}