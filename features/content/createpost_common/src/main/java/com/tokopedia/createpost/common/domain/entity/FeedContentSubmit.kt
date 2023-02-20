package com.tokopedia.createpost.common.domain.entity

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
        var error: String = "",

        @SerializedName("meta")
        @Expose
        var meta: Meta = Meta()
)
