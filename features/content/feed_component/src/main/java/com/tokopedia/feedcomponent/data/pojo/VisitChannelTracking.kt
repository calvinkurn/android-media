package com.tokopedia.feedcomponent.data.pojo

import com.google.gson.annotations.SerializedName


data class VisitChannelTracking(
        @SerializedName("success")
        val success: Boolean = false,
) {

    data class Response(
            @SerializedName("broadcasterReportVisitChannel")
            val reportVisitChannelTracking: VisitChannelTracking
    )
}