package com.tokopedia.affiliate.feature.onboarding.data.pojo.usernamesuggestion

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class BymeGetRecommendedAffiliateName(

        @SerializedName("suggestions")
        @Expose
        var suggestions: List<String> = ArrayList(),

        @SerializedName("error")
        @Expose
        var getUsernameSuggestionError: GetUsernameSuggestionError = GetUsernameSuggestionError()

)
