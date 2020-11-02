package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.socket.PlaySocketEnum
import com.tokopedia.play.broadcaster.socket.PlaySocketType


/**
 * Created by mzennis on 16/09/20.
 */
data class Banned(
        @SerializedName("channel_id")
        val channelId: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("reason")
        val reason: String = "",
        @SerializedName("btn_text")
        val btnText: String = "",
        @SerializedName("timestamp")
        val timestamp: Long = 0L
): PlaySocketType {

    override val type: PlaySocketEnum get() = PlaySocketEnum.Banned
}