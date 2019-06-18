package com.tokopedia.affiliate.feature.onboarding.data.pojo.registerusername

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Error(
        @SerializedName("message")
        @Expose
        var message: String = "",

        @SerializedName("type")
        @Expose
        var type: String = "",

        @SerializedName("code")
        @Expose
        var code: Int = 0
)
