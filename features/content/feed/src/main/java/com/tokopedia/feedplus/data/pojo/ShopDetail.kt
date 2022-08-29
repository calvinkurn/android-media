package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class ShopDetail(
    @SerializedName("id")
    @Expose val id: String = "",

    @SerializedName("name")
    @Expose val name: String = "",

    @SerializedName("avatar")
    @Expose val avatar: String = "",

    @SerializedName("isOfficial")
    @Expose val isOfficial: Boolean = false,

    @SerializedName("isGold")
    @Expose val isGold: Boolean = false,

    @SerializedName("badge")
    @Expose val badgeUrl: String = "",

    @SerializedName("url")
    @Expose val url: String = "",

    @SerializedName("shopLink")
    @Expose val shopLink: String = "",

    @SerializedName("shareLinkDescription")
    @Expose val shareLinkDescription: String = "",

    @SerializedName("shareLinkURL")
    @Expose val shareLinkURL: String = ""
)
