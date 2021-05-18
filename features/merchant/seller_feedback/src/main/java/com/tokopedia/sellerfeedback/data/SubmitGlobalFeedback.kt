package com.tokopedia.sellerfeedback.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitGlobalFeedback(
        @SerializedName("state")
        @Expose
        val state: String = "",
        @SerializedName("error")
        @Expose
        val error: Boolean = false,
        @SerializedName("errorMsg")
        @Expose
        val errorMsg: String = ""
)
