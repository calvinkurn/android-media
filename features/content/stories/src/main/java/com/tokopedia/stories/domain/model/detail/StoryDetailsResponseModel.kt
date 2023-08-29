package com.tokopedia.stories.domain.model.detail

import com.google.gson.annotations.SerializedName

data class StoryDetailsResponseModel(
    @SerializedName("contentStoryDetails")
    val data: ContentStoryDetails,
) {

    data class ContentStoryDetails(
        @SerializedName("meta")
        val meta: Meta,
        @SerializedName("stories")
        val stories: List<Story>,
    ) {

        data class Meta(
            @SerializedName("selectedStoryIndex")
            val selectedStoryIndex: Int,
        )

        data class Story(
            @SerializedName("appLink")
            val appLink: String,
            @SerializedName("author")
            val author: Author,
            @SerializedName("category")
            val category: String,
            @SerializedName("id")
            val id: String,
            @SerializedName("interaction")
            val interaction: Interaction,
            @SerializedName("media")
            val media: Media,
            @SerializedName("meta")
            val meta: MetaX,
            @SerializedName("slug")
            val slug: String,
            @SerializedName("status")
            val status: String,
            @SerializedName("totalProducts")
            val totalProducts: Int,
            @SerializedName("totalProductsFmt")
            val totalProductsFmt: String,
            @SerializedName("webLink")
            val webLink: String,
        ) {

            data class Author(
                @SerializedName("appLink")
                val appLink: String,
                @SerializedName("badgeURL")
                val badgeURL: String,
                @SerializedName("encryptedID")
                val encryptedID: String,
                @SerializedName("hasStory")
                val hasStory: Boolean,
                @SerializedName("id")
                val id: String,
                @SerializedName("isLive")
                val isLive: Boolean,
                @SerializedName("isUnseenStoryExist")
                val isUnseenStoryExist: Boolean,
                @SerializedName("name")
                val name: String,
                @SerializedName("thumbnailURL")
                val thumbnailURL: String,
                @SerializedName("type")
                val type: Int,
                @SerializedName("webLink")
                val webLink: String,
            )

            data class Media(
                @SerializedName("link")
                val link: String,
                @SerializedName("type")
                val type: String,
            )

            data class Interaction(
                @SerializedName("deletable")
                val deletable: Boolean,
                @SerializedName("editable")
                val editable: Boolean,
                @SerializedName("reportable")
                val reportable: Boolean,
                @SerializedName("shareable")
                val shareable: Boolean,
            )

            data class MetaX(
                @SerializedName("hasSeen")
                val hasSeen: Boolean,
                @SerializedName("shareDescription")
                val shareDescription: String,
                @SerializedName("shareImage")
                val shareImage: String,
                @SerializedName("shareTitle")
                val shareTitle: String,
            )
        }
    }
}
