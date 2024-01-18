package com.tokopedia.feedplus.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetContentWidgetRecommendationResponse(
    @SerializedName("contentWidgetRecommendation")
    val contentWidgetRecommendation: Entity = Entity()
) {

    data class Entity(
        @SerializedName("data")
        val data: List<Data> = emptyList()
    )

    data class Data(
        @SuppressLint("Invalid Data Type")
        @SerializedName("contentID")
        val contentID: ContentIdentifier = ContentIdentifier(),

        @SerializedName("__typename")
        val typename: String = "",

        @SerializedName("appLink")
        val appLink: String = "",

        @SerializedName("viewsFmt")
        val viewsFmt: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("author")
        val author: Author = Author(),

        @SerializedName("media")
        val media: Media = Media()
    )

    data class ContentIdentifier(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("origin")
        val origin: String = ""
    )

    data class Author(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("thumbnailURL")
        val thumbnailUrl: String = "",

        @SerializedName("appLink")
        val appLink: String = ""
    )

    data class Media(
        @SerializedName("type")
        val type: String = "",

        @SerializedName("link")
        val link: String = "",

        @SerializedName("coverURL")
        val coverUrl: String = ""
    )
}
