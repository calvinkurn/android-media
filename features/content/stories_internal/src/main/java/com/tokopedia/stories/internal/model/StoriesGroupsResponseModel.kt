package com.tokopedia.stories.internal.model

import com.google.gson.annotations.SerializedName

data class StoriesGroupsResponseModel(
    @SerializedName("contentStoryGroups")
    val data: ContentStoriesGroups = ContentStoriesGroups(),
) {

    data class ContentStoriesGroups(
        @SerializedName("groups")
        val groups: List<Group> = emptyList(),
        @SerializedName("meta")
        val meta: Meta = Meta(),
    )

    data class Group(
        @SerializedName("appLink")
        val appLink: String = "",
        @SerializedName("image")
        val image: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("type")
        val type: String = "",
        @SerializedName("value")
        val value: String = "",
        @SerializedName("webLink")
        val webLink: String = "",
        @SerializedName("hasStory")
        val hasStory: Boolean = false,
        @SerializedName("isUnseenStoryExist")
        val isUnseenStoryExist: Boolean = false,
    )

    data class Meta(
        @SerializedName("selectedGroupIndex")
        val selectedGroupIndex: Int = 0,
        @SerializedName("nextCursor")
        val nextCursor: String = "",
    )
}

