package com.tokopedia.stories.common.domain

import com.google.gson.annotations.SerializedName

/**
 * Created by kenny.hadisaputra on 18/08/23
 */
internal data class GetShopStoriesStatusResponse(
    @SerializedName("contentAuthorStatuses")
    val response: Response = Response(),
) {

    data class Response(
        @SerializedName("data")
        val data: List<Data> = emptyList(),

        @SerializedName("meta")
        val meta: Meta = Meta(),
    )

    data class Data(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("hasStory")
        val hasStory: Boolean = false,

        @SerializedName("isUnseenStoryExist")
        val isUnseenStoryExist: Boolean = false,

        @SerializedName("appLink")
        val appLink: String = "",
    )

    data class Meta(
        @SerializedName("coachmark")
        val coachMark: String = "",
    )
}
