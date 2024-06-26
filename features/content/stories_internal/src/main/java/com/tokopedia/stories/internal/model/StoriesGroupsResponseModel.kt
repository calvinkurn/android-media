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
        @SerializedName("author")
        val author: Author = Author(),
    )

    data class Author(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("type")
        val type: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("badgeURL")
        val badgeURL: String = "",
        @SerializedName("hasStory")
        val hasStory: Boolean = false,
        @SerializedName("isUnseenStoryExist")
        val isUnseenStoryExist: Boolean = false,
        @SerializedName("thumbnailURL")
        val thumbnailURL: String = "",
        @SerializedName("appLink")
        val appLink: String = "",
    )

    data class Meta(
        @SerializedName("selectedGroupIndex")
        val selectedGroupIndex: Int = 0,
        @SerializedName("nextCursor")
        val nextCursor: String = "",
    )
}

