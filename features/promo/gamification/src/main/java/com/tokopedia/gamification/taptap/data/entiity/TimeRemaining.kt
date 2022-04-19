package com.tokopedia.gamification.taptap.data.entiity

import com.google.gson.annotations.SerializedName

data class TimeRemaining(
        @SerializedName("backgroundColor")
        val backgroundColor: String? = null,

        @SerializedName("fontColor")
        val fontColor: String? = null,

        @SerializedName("borderColor")
        val borderColor: String? = null,

        @SerializedName("isShow")
        val isShow: Boolean = false,

        @SerializedName("seconds")
        val seconds: Long = 0L,

        @SerializedName("unixFetch")
        val unixFetch: Long = 0L

)