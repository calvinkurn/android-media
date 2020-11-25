package com.tokopedia.discovery2.data.notifier

import com.google.gson.annotations.SerializedName

data class NotifierSetReminder(
        @SerializedName("error_message")
        val errorMessage: String? = null,
        @SerializedName("is_success")
        val isSuccess: Int? = null
)
