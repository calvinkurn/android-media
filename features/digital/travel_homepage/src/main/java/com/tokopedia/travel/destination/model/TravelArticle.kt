package com.tokopedia.travel.destination.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-12-23
 */

data class TravelArticle (
        @SerializedName("items")
        @Expose
        val items: List<Item> = listOf(),

        @SerializedName("meta")
        @Expose
        val meta: Meta = Meta()
) {
    data class Response(
            @SerializedName("TravelArticle")
            @Expose
            val response: TravelArticle = TravelArticle()
    )

    data class Item (
            @SerializedName("tag")
            @Expose
            val tag: Int = 0,

            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("publishedDate")
            @Expose
            val publishedDate: String = "",

            @SerializedName("webURL")
            @Expose
            val webUrl: String = "",

            @SerializedName("appURL")
            @Expose
            val appUrl: String = "",

            @SerializedName("imageURL")
            @Expose
            val imageUrl: String = ""
    )

    data class Meta(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("URLTitle")
            @Expose
            val urlTitle: String = "",

            @SerializedName("webURL")
            @Expose
            val webUrl: String = "",

            @SerializedName("appURL")
            @Expose
            val appUrl: String = ""
    )
}