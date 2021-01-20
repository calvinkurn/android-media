package com.tokopedia.shop.pageheader.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FollowShopResponse (
    @SerializedName("followShop")
    @Expose
    val followShop: FollowShop?
)

data class FollowShop(
        @SerializedName("success")
        @Expose
        val success: Boolean?,
        @SerializedName("message")
        @Expose
        val message: String?,
        @SerializedName("isFirstTime")
        @Expose
        val isFirstTime: Boolean?,
        @SerializedName("isFollowing")
        @Expose
        val isFollowing: Boolean?,
        @SerializedName("buttonLabel")
        @Expose
        val buttonLabel: String?,
        @SerializedName("toaster")
        @Expose
        val toaster: Toaster?
)

data class Toaster(
        @SerializedName("toasterText")
        @Expose
        val toasterText: String?,
        @SerializedName("buttonType")
        @Expose
        val buttonType: String?,
        @SerializedName("buttonLabel")
        @Expose
        val buttonLabel: String?,
        @SerializedName("url")
        @Expose
        val url: String?,
        @SerializedName("appLink")
        @Expose
        val appLink: String?
)