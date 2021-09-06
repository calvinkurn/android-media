package com.tokopedia.play.data.realtimenotif

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 03/08/21
 */
data class RealTimeNotification(
        @SerializedName("type")
        val type: String = "",

        @SerializedName("copy")
        val copy: String = "",

        @SerializedName("icon")
        val icon: String = "",

        @SerializedName("background_color")
        val backgroundColor: String = "",
)