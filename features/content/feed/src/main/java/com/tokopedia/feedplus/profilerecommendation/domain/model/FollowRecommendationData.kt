package com.tokopedia.feedplus.profilerecommendation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-09-11.
 */
data class FollowRecommendationData(
        @SerializedName("header")
        @Expose
        val header: FollowRecommendationHeader,

        @SerializedName("media")
        @Expose
        val media: List<FollowRecommendationMedia> = emptyList()
)