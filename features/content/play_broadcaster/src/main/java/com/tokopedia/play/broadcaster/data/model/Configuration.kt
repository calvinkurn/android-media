package com.tokopedia.play.broadcaster.data.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 26/05/20.
 */
data class Configuration(
        @SerializedName("")
        val isUserWhitelisted: Boolean = false,
        @SerializedName("")
        val isHaveOnGoingLive: Boolean = false,
        @SerializedName("")
        val isOfficial: Boolean = false,
        @SerializedName("")
        val channelId: String = "",
        @SerializedName("")
        val maxTaggedProduct: Int = 0,
        @SerializedName("")
        val maxLiveStreamDuration: Long = 0,
        @SerializedName("")
        val countDownDuration: Long = 0
)