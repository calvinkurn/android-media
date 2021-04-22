package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 22/04/21
 */
data class GetAddedChannelTagsResponse(
        @SerializedName("broadcasterGetChannelTags")
        val recommendedTags: GetAddedTags = GetAddedTags()
) {

    data class GetAddedTags(
            @SerializedName("tags")
            val tags: List<String> = emptyList()
    )
}