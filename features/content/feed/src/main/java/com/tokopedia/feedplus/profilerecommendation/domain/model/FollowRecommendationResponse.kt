package com.tokopedia.feedplus.profilerecommendation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-09-11.
 */
data class FollowRecommendationResponse(
        @SerializedName("feedUserOnboardingRecommendations")
        @Expose
        val feedUserOnboardingRecommendations: FollowRecommendationQuery
)