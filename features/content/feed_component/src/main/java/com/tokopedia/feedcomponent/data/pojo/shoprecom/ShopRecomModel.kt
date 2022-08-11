package com.tokopedia.feedcomponent.data.pojo.shoprecom

import com.google.gson.annotations.SerializedName

data class UserShopRecomModel(
    @SerializedName("feedXRecomWidget")
    val feedXRecomWidget: FeedXRecomWidget = FeedXRecomWidget(),
)

data class FeedXRecomWidget(
    @SerializedName("isShown")
    val isShown: Boolean = false,
    @SerializedName("items")
    val items: List<ShopRecomItem> = emptyList(),
    @SerializedName("nextCursor")
    val nextCursor: String = "",
    @SerializedName("title")
    val title: String = "",
)

data class ShopRecomItem(
    @SerializedName("badgeImageURL")
    val badgeImageURL: String = "",
    @SerializedName("encryptedID")
    val encryptedID: String = "",
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("logoImageURL")
    val logoImageURL: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("nickname")
    val nickname: String = "",
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("applink")
    val applink: String = "",
)