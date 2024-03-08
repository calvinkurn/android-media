package com.tokopedia.chatbot.chatbot2.data.slowmode

import com.google.gson.annotations.SerializedName

data class DynamicAttachmentSlowMode(
    @SerializedName("is_using_slow_mode")
    val isUsingSlowMode: Boolean = false,
    @SerializedName("slow_mode_duration_in_seconds")
    val slowModeDurationInSeconds: Int = 0
)
