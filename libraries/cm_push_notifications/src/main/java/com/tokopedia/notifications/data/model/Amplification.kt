package com.tokopedia.notifications.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Amplification(
        @Expose @SerializedName("status") val status: Int = 0,
        @Expose @SerializedName("error_message") val errorMessage: String = "",
        @Expose @SerializedName("push_data") val pushData: List<String> = emptyList()
)