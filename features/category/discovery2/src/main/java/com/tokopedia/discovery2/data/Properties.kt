package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class Properties(
        @SerializedName("columns")
        val columns: String?,

        @SerializedName("button_notification")
        val buttonNotification: Boolean?,

        @SerializedName("registered_message")
        val registeredMessage: String?,

        @SerializedName("unregistered_message")
        val unregisteredMessage: String?
)