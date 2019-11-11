package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-10-02
 */

data class TravelCrossSelling(
        @SerializedName("items")
        @Expose
        val items: List<Item> = listOf(),

        @SerializedName("meta")
        @Expose
        val meta: Meta = Meta()
) {
    data class Item(
            @SerializedName("product")
            @Expose
            val product: String = "",

            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("content")
            @Expose
            val content: String = "",

            @SerializedName("prefix")
            @Expose
            val prefix: String = "",

            @SerializedName("imageURL")
            @Expose
            val imageUrl: String = "",

            @SerializedName("uriWeb")
            @Expose
            val uriWeb: String = "",

            @SerializedName("uri")
            @Expose
            val uri: String = "",

            @SerializedName("value")
            @Expose
            val value: String = ""
    )

    data class Meta(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("uri")
            @Expose
            val uri: String = "",

            @SerializedName("uriWeb")
            @Expose
            val uriWeb: String = ""
    )

    class Response(
            @SerializedName("crossSell")
            @Expose
            val response: TravelCrossSelling = TravelCrossSelling()
    )
}