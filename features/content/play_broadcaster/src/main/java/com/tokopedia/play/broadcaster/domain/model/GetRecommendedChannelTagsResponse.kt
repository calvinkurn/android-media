package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 22/04/21
 */
data class GetRecommendedChannelTagsResponse(
        @SerializedName("broadcasterGetRecommendedTags")
        val recommendedTags: GetRecommendedTags = GetRecommendedTags()
) {

    data class GetRecommendedTags(
        @SerializedName("tags")
        val tags: List<String> = emptyList(),

        @SerializedName("minTags")
        val minTags: Int = 0,

        @SerializedName("maxTags")
        val maxTags: Int = 0,
    )
}
