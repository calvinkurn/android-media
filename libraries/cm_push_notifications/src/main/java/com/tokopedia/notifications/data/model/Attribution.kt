package com.tokopedia.notifications.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Attribution(
        @Expose @SerializedName("is_success") val isSuccess: Boolean = false,
        @Expose @SerializedName("error_message") val errorMessage: String = ""
)