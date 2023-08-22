package com.tokopedia.stories.domain.model.group

import com.google.gson.annotations.SerializedName

data class StoryGroupsResponseModel(
    @SerializedName("contentStoryGroups")
    val data: ContentStoryGroups,
) {

    data class ContentStoryGroups(
        @SerializedName("groups")
        val groups: List<Group>,
        @SerializedName("meta")
        val meta: Meta,
    )

    data class Group(
        @SerializedName("appLink")
        val appLink: String,
        @SerializedName("image")
        val image: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("value")
        val value: String,
        @SerializedName("webLink")
        val webLink: String,
    )

    data class Meta(
        @SerializedName("selectedGroupIndex")
        val selectedGroupIndex: Int,
    )
}

