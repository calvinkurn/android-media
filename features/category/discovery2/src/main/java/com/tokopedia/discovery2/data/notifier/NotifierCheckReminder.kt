package com.tokopedia.discovery2.data.notifier

import com.google.gson.annotations.SerializedName

data class NotifierCheckReminder(
        @SerializedName("error_message")
        val errorMessage: String? = null,
        @SerializedName("is_success")
        val isSuccess: Int? = null,
        @SerializedName("status")
        val status: Int? = null
)
