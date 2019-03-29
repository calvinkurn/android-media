package com.tokopedia.interestpick.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FeedInterestUserUpdate(
        @SerializedName("success")
        @Expose
        val success: Boolean = false,

        @SerializedName("error")
        @Expose
        val error: String = ""
)