package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.socket.PlaySocketEnum
import com.tokopedia.play.broadcaster.socket.PlaySocketType


/**
 * Created by mzennis on 22/06/20.
 */
data class TotalView(
        @SerializedName("total_view")
        val totalView: Long = 0,
        @SerializedName("total_view_formatted")
        val totalViewFmt: String = ""
): PlaySocketType {
        override val type: PlaySocketEnum get() = PlaySocketEnum.TotalView
}