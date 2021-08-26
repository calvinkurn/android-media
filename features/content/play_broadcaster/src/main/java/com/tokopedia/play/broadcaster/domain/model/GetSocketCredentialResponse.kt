package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play_common.websocket.DEFAULT_DELAY
import com.tokopedia.play_common.websocket.DEFAULT_MAX_RETRIES
import com.tokopedia.play_common.websocket.DEFAULT_PING


/**
 * Created by mzennis on 30/06/20.
 */
data class GetSocketCredentialResponse(
        @SerializedName("playGetSocketCredential")
        val socketCredential: SocketCredential
) {
    data class SocketCredential(
            @SerializedName("gc_token")
            val gcToken: String = "",
            @SerializedName("setting")
            val setting: Setting = Setting()
    )

    data class Setting(
            @SerializedName("ping_interval")
            val pingInterval: Long = DEFAULT_PING,
            @SerializedName("max_chars")
            val maxChars: Int = 200,
            @SerializedName("max_retries")
            val maxRetries: Int = DEFAULT_MAX_RETRIES,
            @SerializedName("min_reconnect_delay")
            val minReconnectDelay: Int = DEFAULT_DELAY
    )
}