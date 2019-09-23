package com.tokopedia.feedplus.profilerecommendation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-09-11.
 */
data class FollowRecommendationQuery(

        @SerializedName("meta")
        @Expose
        val meta: FollowRecommendationMeta = FollowRecommendationMeta(),

        @SerializedName("data")
        @Expose
        val data: List<FollowRecommendationData> = emptyList()
)