package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.socket.PlaySocketEnum
import com.tokopedia.play.broadcaster.socket.PlaySocketType


/**
 * Created by mzennis on 24/09/20.
 */
data class Freeze(
        @SerializedName("channel_id")
        val channelId: String = "",
        @SerializedName("is_freeze")
        val isFreeze: Boolean = false
): PlaySocketType {

    override val type: PlaySocketEnum get() = PlaySocketEnum.Freeze

}