package com.tokopedia.tokopedianow.recipebookmark.domain.model

import com.google.gson.annotations.SerializedName

data class GetRecipeBookmarksResponse(
    @SerializedName("TokonowGetRecipeBookmarks")
    val tokonowGetRecipeBookmarks: TokonowGetRecipeBookmarks
) {
    data class TokonowGetRecipeBookmarks(
        @SerializedName("data")
        val data: Data,
        @SerializedName("header")
        val header: Header,
        @SerializedName("metadata")
        val metadata: Metadata
    ) {
        data class Header(
            @SerializedName("message")
            val message: String,
            @SerializedName("statusCode")
            val statusCode: String,
            @SerializedName("success")
            val success: Boolean
        )
        data class Metadata(
            @SerializedName("hasNext")
            val hasNext: Boolean
        )
        data class Data(
            @SerializedName("recipes")
            val recipes: List<Recipe>,
            @SerializedName("userID")
            val userID: String
        ) {
            data class Recipe(
                @SerializedName("duration")
                val duration: Int?,
                @SerializedName("id")
                val id: String,
                @SerializedName("images")
                val images: List<Image>?,
                @SerializedName("portion")
                val portion: Int,
                @SerializedName("tags")
                val tags: List<Tag>?,
                @SerializedName("title")
                val title: String,
                @SerializedName("appUrl")
                val appUrl: String
            ) {
                data class Image(
                    @SerializedName("urlOriginal")
                    val urlOriginal: String
                )
                data class Tag(
                    @SerializedName("id")
                    val id: String,
                    @SerializedName("name")
                    val name: String
                )
            }
        }
    }
}