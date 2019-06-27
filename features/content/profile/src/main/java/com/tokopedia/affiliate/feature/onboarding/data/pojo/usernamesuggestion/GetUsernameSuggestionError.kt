package com.tokopedia.affiliate.feature.onboarding.data.pojo.usernamesuggestion

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetUsernameSuggestionError(
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
