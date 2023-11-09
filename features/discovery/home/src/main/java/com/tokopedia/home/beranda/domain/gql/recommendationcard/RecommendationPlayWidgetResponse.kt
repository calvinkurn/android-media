package com.tokopedia.home.beranda.domain.gql.recommendationcard

import com.google.gson.annotations.SerializedName

data class RecommendationPlayWidgetResponse(
    @SerializedName("activityID")
    val activityID: String = "",
    @SerializedName("author")
    val author: Author = Author(),
    @SerializedName("basic")
    val basic: Basic = Basic(),
    @SerializedName("category")
    val category: Category = Category(),
    @SerializedName("contentOriginID")
    val contentOriginID: String = "",
    @SerializedName("contentOriginType")
    val contentOriginType: String = "",
    @SerializedName("contentTypeL2")
    val contentTypeL2: String = "",
    @SerializedName("feedsContentType")
    val feedsContentType: Int = 0,
    @SerializedName("id")
    val id: String = "",
    @SerializedName("categoryID")
    val categoryID: String = "",
    @SerializedName("link")
    val link: Link = Link(),
    @SerializedName("product")
    val product: Product = Product(),
    @SerializedName("promo")
    val promo: Promo = Promo(),
    @SerializedName("recommendationType")
    val recommendationType: String = "",
    @SerializedName("share")
    val share: Share = Share(),
    @SerializedName("stats")
    val stats: Stats = Stats()
) {
    data class Author(
        @SerializedName("appLink")
        val appLink: String = "",
        @SerializedName("badge")
        val badge: String = "",
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("ownerID")
        val ownerID: String = "",
        @SerializedName("thumbnailURL")
        val thumbnailURL: String = "",
        @SerializedName("type")
        val type: Type = Type(),
        @SerializedName("webLink")
        val webLink: String = ""
    )

    data class Basic(
        @SerializedName("activeMediaID")
        val activeMediaID: String = "",
        @SerializedName("autoPlay")
        val autoPlay: Boolean = false,
        @SerializedName("coverURL")
        val coverURL: String = "",
        @SerializedName("endTime")
        val endTime: String = "",
        @SerializedName("groupID")
        val groupID: String = "",
        @SerializedName("pinnedMessage")
        val pinnedMessage: PinnedMessage = PinnedMessage(),
        @SerializedName("startTime")
        val startTime: String = "",
        @SerializedName("status")
        val status: Status = Status(),
        @SerializedName("timestamp")
        val timestamp: Timestamp = Timestamp(),
        @SerializedName("title")
        val title: String = "",
        @SerializedName("type")
        val type: Type = Type()
    )

    data class Category(
        @SerializedName("availableL1")
        val availableL1: List<String> = emptyList(),
        @SerializedName("availableL2")
        val availableL2: List<String> = emptyList(),
        @SerializedName("availableL3")
        val availableL3: List<String> = emptyList(),
        @SerializedName("dominantL1")
        val dominantL1: List<String> = emptyList(),
        @SerializedName("dominantL2")
        val dominantL2: List<String> = emptyList(),
        @SerializedName("dominantL3")
        val dominantL3: List<String> = emptyList()
    )

    data class Link(
        @SerializedName("applink")
        val applink: String = "",
        @SerializedName("weblink")
        val weblink: String = ""
    )

    data class Product(
        @SerializedName("productAttributes")
        val productAttributes: List<ProductAttribute> = emptyList(),
        @SerializedName("totalProduct")
        val totalProduct: String = ""
    ) {
        data class ProductAttribute(
            @SerializedName("productID")
            val productID: String = ""
        )
    }

    data class Share(
        @SerializedName("metaTitle")
        val metaTitle: String = "",
        @SerializedName("redirectURL")
        val redirectURL: String = "",
        @SerializedName("text")
        val text: String = ""
    )

    data class Status(
        @SerializedName("id")
        val id: String = "0",
        @SerializedName("text")
        val text: String = ""
    )
}

data class Promo(
    @SerializedName("state")
    val state: State = State()
) {
    data class State(
        @SerializedName("description")
        val description: String = "",
        @SerializedName("id")
        val id: String = "0"
    )
}

data class Stats(
    @SerializedName("commentFmt")
    val commentFmt: String = "",
    @SerializedName("like")
    val like: String = "",
    @SerializedName("likeFmt")
    val likeFmt: String = "",
    @SerializedName("view")
    val view: String = "",
    @SerializedName("viewFmt")
    val viewFmt: String = ""
)

data class Timestamp(
    @SerializedName("createdAt")
    val createdAt: String = "",
    @SerializedName("publishedAt")
    val publishedAt: String = ""
)

data class Type(
    @SerializedName("id")
    val id: String = "0",
    @SerializedName("text")
    val text: String = ""
)

class PinnedMessage
