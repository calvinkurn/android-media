package com.tokopedia.home.beranda.domain.gql.recommendationcard


import com.google.gson.annotations.SerializedName

data class PlayVideoWidgetResponse(
    @SerializedName("activityID")
    val activityID: String = "0",
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
    @SerializedName("link")
    val link: Link = Link(),
    @SerializedName("medias")
    val medias: List<Media> = listOf(),
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
        val activeMediaID: Int = 0,
        @SerializedName("autoPlay")
        val autoPlay: Boolean = false,
        @SerializedName("isLive")
        val isLive: Boolean = false,
        @SerializedName("coverURL")
        val coverURL: String = "",
        @SerializedName("endTime")
        val endTime: String = "",
        @SerializedName("groupID")
        val groupID: String = "0",
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
    ) {
        class PinnedMessage

        data class Timestamp(
            @SerializedName("createdAt")
            val createdAt: String = "",
            @SerializedName("publishedAt")
            val publishedAt: String = ""
        )
    }

    data class Category(
        @SerializedName("availableL1")
        val availableL1: List<Int> = listOf(),
        @SerializedName("availableL2")
        val availableL2: List<Int> = listOf(),
        @SerializedName("availableL3")
        val availableL3: List<Int> = listOf(),
        @SerializedName("dominantL1")
        val dominantL1: List<Int> = listOf(),
        @SerializedName("dominantL2")
        val dominantL2: List<Int> = listOf(),
        @SerializedName("dominantL3")
        val dominantL3: List<Int> = listOf()
    )

    data class Link(
        @SerializedName("applink")
        val applink: String = "",
        @SerializedName("weblink")
        val weblink: String = ""
    )

    data class Media(
        @SerializedName("id")
        val id: String = "0",
        @SerializedName("mediaURL")
        val mediaURL: String = "",
        @SerializedName("orientation")
        val orientation: Orientation = Orientation(),
        @SerializedName("type")
        val type: Type = Type()
    ) {
        data class Orientation(
            @SerializedName("text")
            val text: String = ""
        )
    }

    data class Product(
        @SerializedName("productAttributes")
        val productAttributes: List<ProductAttribute> = listOf(),
        @SerializedName("totalProduct")
        val totalProduct: Int = 0
    ) {
        data class ProductAttribute(
            @SerializedName("discountPercentage")
            val discountPercentage: Int = 0,
            @SerializedName("productID")
            val productID: Long = 0
        )
    }

    data class Promo(
        @SerializedName("state")
        val state: State = State()
    )

    data class Share(
        @SerializedName("metaTitle")
        val metaTitle: String = "",
        @SerializedName("redirectURL")
        val redirectURL: String = "",
        @SerializedName("text")
        val text: String = ""
    )

    data class State(
        @SerializedName("description")
        val description: String = "",
        @SerializedName("id")
        val id: String = "0"
    )

    data class Stats(
        @SerializedName("commentFmt")
        val commentFmt: String = "",
        @SerializedName("like")
        val like: Int = 0,
        @SerializedName("likeFmt")
        val likeFmt: String = "",
        @SerializedName("view")
        val view: Int = 0,
        @SerializedName("viewFmt")
        val viewFmt: String = ""
    )

    data class Status(
        @SerializedName("id")
        val id: String = "0",
        @SerializedName("text")
        val text: String = ""
    )
}

data class Type(
    @SerializedName("id")
    val id: String = "0",
    @SerializedName("text")
    val text: String = ""
)
