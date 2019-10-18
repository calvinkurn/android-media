package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-10-02
 */

data class TravelCrossSelling(
        @SerializedName("items")
        val items: List<Item> = listOf(),

        @SerializedName("meta")
        val meta: Meta = Meta()
) {
    data class Item(
            @SerializedName("product")
            val product: String = "",

            @SerializedName("title")
            val title: String = "",

            @SerializedName("content")
            val content: String = "",

            @SerializedName("prefix")
            val prefix: String = "",

            @SerializedName("imageURL")
            val imageUrl: String = "",

            @SerializedName("uriWeb")
            val uriWeb: String = "",

            @SerializedName("uri")
            val uri: String = "",

            @SerializedName("value")
            val value: String = ""
    )

    data class Meta(
            @SerializedName("title")
            val title: String = "",

            @SerializedName("uri")
            val uri: String = "",

            @SerializedName("uriWeb")
            val uriWeb: String = ""
    )

    class Response(
            @SerializedName("crossSell")
            val response: TravelCrossSelling = TravelCrossSelling()
    )
}