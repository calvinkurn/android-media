package com.tokopedia.affiliate.feature.onboarding.data.pojo.usernamesuggestion

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetUsernameSuggestionData(

        @SerializedName("bymeGetRecommendedAffiliateName")
        @Expose
        var suggestion: BymeGetRecommendedAffiliateName = BymeGetRecommendedAffiliateName()

)
