package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 30/06/20.
 */
data class GetSocketCredentialResponse(
        @SerializedName("playGetSocketCredential")
        val socketCredential: SocketCredential
) {
    data class SocketCredential(
            @SerializedName("header")
            val header: Header = Header(),
            @SerializedName("data")
            val data: Credential = Credential()
    )

    data class Header(
            @SerializedName("status")
            val status: Int = 0,
            @SerializedName("message")
            val message: String = ""
    )

    data class Credential(
            @SerializedName("gc_token")
            val gc_token: String = "",
            @SerializedName("setting")
            val setting: Setting = Setting()
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