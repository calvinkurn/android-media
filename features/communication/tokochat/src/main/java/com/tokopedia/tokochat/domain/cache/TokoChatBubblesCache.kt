package com.tokopedia.tokochat.domain.cache

import com.google.gson.annotations.SerializedName

data class TokoChatBubblesCache(
    @SerializedName("channelId")
    val channelId: String = "",
    @SerializedName("hasShownBottomSheet")
    val hasShownBottomSheet: Boolean = false,
    @SerializedName("hasShownTicker")
    val hasShownTicker: Boolean? = null
)
