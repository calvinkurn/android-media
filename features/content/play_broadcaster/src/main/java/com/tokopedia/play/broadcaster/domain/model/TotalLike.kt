package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.socket.PlaySocketEnum
import com.tokopedia.play.broadcaster.socket.PlaySocketType


/**
 * Created by mzennis on 06/07/20.
 */
data class TotalLike(
        @SerializedName("total_like")
        val totalLike: Long = 0,
        @SerializedName("total_like_formatted")
        val totalLikeFmt: String = ""
): PlaySocketType {
    override val type: PlaySocketEnum get() = PlaySocketEnum.TotalLike
}