package com.tokopedia.feedcomponent.data.pojo.people

import com.google.gson.annotations.SerializedName

data class ShopRecomItemUI(
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
)
