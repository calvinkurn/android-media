package com.tokopedia.stories.domain.model.detail

import com.google.gson.annotations.SerializedName

data class StoriesDetailsResponseModel(
    @SerializedName("contentStoryDetails")
    val data: ContentStoriesDetails = ContentStoriesDetails(),
) {

    data class ContentStoriesDetails(
        @SerializedName("meta")
        val meta: Meta = Meta(),
        @SerializedName("stories")
        val stories: List<Stories> = emptyList(),
    ) {

        data class Meta(
            @SerializedName("selectedStoryIndex")
            val selectedStoriesIndex: Int = -1,
        )

        data class Stories(
            @SerializedName("appLink")
            val appLink: String = "",
            @SerializedName("author")
            val author: Author = Author(),
            @SerializedName("category")
            val category: String = "",
            @SerializedName("id")
            val id: String = "",
            @SerializedName("interaction")
            val interaction: Interaction = Interaction(),
            @SerializedName("media")
            val media: Media = Media(),
            @SerializedName("meta")
            val meta: MetaX = MetaX(),
            @SerializedName("slug")
            val slug: String = "",
            @SerializedName("status")
            val status: String = "",
            @SerializedName("totalProducts")
            val totalProducts: Int = -1,
            @SerializedName("totalProductsFmt")
            val totalProductsFmt: String = "",
            @SerializedName("webLink")
            val webLink: String = "",
        ) {

            data class Author(
                @SerializedName("appLink")
                val appLink: String = "",
                @SerializedName("badgeURL")
                val badgeURL: String = "",
                @SerializedName("encryptedID")
                val encryptedID: String = "",
                @SerializedName("hasStory")
                val hasStories: Boolean = false,
                @SerializedName("id")
                val id: String = "",
                @SerializedName("isLive")
                val isLive: Boolean = false,
                @SerializedName("isUnseenStoryExist")
                val isUnseenStoriesExist: Boolean = false,
                @SerializedName("name")
                val name: String = "",
                @SerializedName("thumbnailURL")
                val thumbnailURL: String = "",
                @SerializedName("type")
                val type: Int = -1,
                @SerializedName("webLink")
                val webLink: String = "",
            )

            data class Media(
                @SerializedName("link")
                val link: String = "",
                @SerializedName("type")
                val type: String = "",
            )

            data class Interaction(
                @SerializedName("deletable")
                val deletable: Boolean = false,
                @SerializedName("editable")
                val editable: Boolean = false,
                @SerializedName("reportable")
                val reportable: Boolean = false,
                @SerializedName("shareable")
                val shareable: Boolean = false,
            )

            data class MetaX(
                @SerializedName("hasSeen")
                val hasSeen: Boolean = false,
                @SerializedName("shareDescription")
                val shareDescription: String = "",
                @SerializedName("shareImage")
                val shareImage: String = "",
                @SerializedName("shareTitle")
                val shareTitle: String = "",
                @SerializedName("activityTracker")
                val activityTracker: String = "",
                @SerializedName("templateTracker")
                val templateTracker: String = "",
            )
        }
    }
}
