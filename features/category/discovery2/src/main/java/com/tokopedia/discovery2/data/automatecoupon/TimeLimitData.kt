package com.tokopedia.discovery2.data.automatecoupon

import com.google.gson.annotations.SerializedName

data class TimeLimitData(
    @SerializedName("title")
    val title: String = "",

    @SerializedName("timestamp")
    val timestamp: Long = 0L,

    @SerializedName("show_timer")
    val showTimer: Boolean = true
)
