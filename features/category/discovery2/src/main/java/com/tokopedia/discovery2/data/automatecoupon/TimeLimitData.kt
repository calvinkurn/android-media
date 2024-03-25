package com.tokopedia.discovery2.data.automatecoupon

import com.google.gson.annotations.SerializedName

data class TimeLimitData(
    @SerializedName("title")
    val title: String = "",

    @SerializedName("value")
    val timestamp: Long = 0L,

    @SerializedName("show_timer")
    private val showTimer: Boolean = false
) {
    fun canShowTimer() = showTimer && timestamp > 0L
}
