package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName

data class SocketCredential(
        @SerializedName("gc_token")
        val gcToken: String = "",
        @SerializedName("setting")
        val setting: Setting = Setting()
) {

    data class Response(
            @SerializedName("playGetSocketCredential")
            val socketCredential: SocketCredential
    )

    data class Setting(
            @SerializedName("ping_interval")
            val pingInterval: Long = 0,
            @SerializedName("max_chars")
            val maxChars: Int = 0,
            @SerializedName("max_retries")
            val maxRetries: Int = 0,
            @SerializedName("min_reconnect_delay")
            val minReconnectDelay: Int = 0
    )
}