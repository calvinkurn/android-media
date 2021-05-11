package com.tokopedia.play.broadcaster.util

import com.google.gson.Gson
import com.tokopedia.play.broadcaster.domain.model.GetRecommendedChannelTagsResponse

/**
 * Created by jegul on 11/05/21
 */
class PlayBroadcasterResponseBuilder {

    private val gson = Gson()

    fun buildRecommendedChannelTagsResponse(tags: List<String>): GetRecommendedChannelTagsResponse {
        return GetRecommendedChannelTagsResponse(
                recommendedTags = GetRecommendedChannelTagsResponse.GetRecommendedTags(
                        tags = tags
                )
        )
    }
}