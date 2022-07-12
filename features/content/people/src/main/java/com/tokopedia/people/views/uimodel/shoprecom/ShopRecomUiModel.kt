package com.tokopedia.people.views.uimodel.shoprecom

import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem

data class ShopRecomUiModel(
    @SerializedName("isShown")
    val isShown: Boolean = false,
    @SerializedName("items")
    val items: List<ShopRecomUiModelItem> = emptyList(),
    @SerializedName("nextCursor")
    val nextCursor: String = "",
    @SerializedName("title")
    val title: String = "",
)

data class ShopRecomUiModelItem(
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
    @SerializedName("isFollow")
    var isFollow: Boolean = false,
): BaseItem()
