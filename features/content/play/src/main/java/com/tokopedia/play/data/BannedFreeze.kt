package com.tokopedia.play.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-13.
 */
data class BannedFreeze(
        @SerializedName("is_freeze")
        @Expose
        val isFreeze: Boolean = false,
        @SerializedName("is_banned")
        @Expose
        val isBanned: Boolean = false,
        @SerializedName("channel_id")
        @Expose
        val channelId: String = "",
        @SerializedName("user_id")
        @Expose
        val userId: String = ""
)