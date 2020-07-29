package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.socket.PlaySocketEnum
import com.tokopedia.play.broadcaster.socket.PlaySocketType


/**
 * Created by mzennis on 19/06/20.
 */
data class LiveDuration(
        @SerializedName("duration_sec")
        val duration: Long,
        @SerializedName("max_duration_sec")
        val maxDuration: Long,
        @SerializedName("remaining_sec")
        val remaining: Long,
        @SerializedName("start_time")
        val startTime: String,
        @SerializedName("time_now")
        val timeNow: String
): PlaySocketType {
        override val type: PlaySocketEnum get() = PlaySocketEnum.LiveDuration
}