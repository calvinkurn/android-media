package com.tokopedia.shop.common.data.source.cloud.model.followshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
