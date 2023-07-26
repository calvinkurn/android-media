package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on July 26, 2023
 */
data class FeedXRecomWidgetEntity(
    @SerializedName("feedXRecomWidget")
    val wrapper: Wrapper = Wrapper()
) {

    data class Wrapper(
        @SerializedName("isShown")
        val isShown: Boolean = false,

        @SerializedName("title")
        val title: String = "",

        @SerializedName("subtitle")
        val subtitle: String = "",

        @SerializedName("seeAll")
        val seeAll: SeeAll = SeeAll(),

        @SerializedName("items")
        val items: List<Item> = emptyList(),

        @SerializedName("nextCursor")
        val nextCursor: String = "",
    )

    data class SeeAll(
        @SerializedName("isShown")
        val isShown: Boolean = false,

        @SerializedName("text")
        val text: String = "",

        @SerializedName("weblink")
        val weblink: String = "",

        @SerializedName("applink")
        val applink: String = "",
    )

    data class Item(
        @SerializedName("type")
        val type: Int = 0,

        @SerializedName("id")
        val id: String = "",

        @SerializedName("encryptedID")
        val encryptedId: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("nickname")
        val nickname: String = "",

        @SerializedName("badgeImageURL")
        val badgeImageUrl: String = "",

        @SerializedName("logoImageURL")
        val logoImageUrl: String = "",

        @SerializedName("weblink")
        val weblink: String = "",

        @SerializedName("applink")
        val applink: String = "",

        @SerializedName("coverURL")
        val coverUrl: String = "",

        @SerializedName("mediaURL")
        val mediaUrl: String = "",
    )
}
