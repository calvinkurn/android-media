package com.tokopedia.affiliatecommon.data.pojo.submitpost.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FeedContentSubmit(
        @SerializedName("success")
        @Expose
        var success: Int = 0,

        @SerializedName("redirectURI")
        @Expose
        var redirectURI: String = "",

        @SerializedName("error")
        @Expose
        var error: String = ""
)