package com.tokopedia.feedplus.profilerecommendation.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-09-11.
 */
data class FollowRecommendationMeta(
        @SerializedName("assets")
        @Expose
        val assets: FollowRecommendationAsset = FollowRecommendationAsset(),

        @SerializedName("minFollowed")
        @Expose
        val minFollowed: Int = 0,

        @SerializedName("nextCursor")
        @Expose
        val nextCursor: String = "",

        @SerializedName("source")
        @Expose
        val source: String = ""
)