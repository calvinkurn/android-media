package com.tokopedia.notifications.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Amplification(
        @Expose @SerializedName("status") val status: Int = 0,
        @Expose @SerializedName("error_message") val errorMessage: String = "",
        @Expose @SerializedName("push_data") val pushData: List<String> = emptyList(),
        @Expose @SerializedName("inapp_data") val inAppData: List<String> = emptyList(),
        @Expose @SerializedName("next_fetch") val nextFetch: Long = 0
)