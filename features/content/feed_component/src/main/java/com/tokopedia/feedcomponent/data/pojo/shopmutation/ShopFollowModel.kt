package com.tokopedia.feedcomponent.data.pojo.shopmutation

import com.google.gson.annotations.SerializedName

data class ShopFollowModel(
    @SerializedName("followShop")
    val followShop: FollowShop = FollowShop(),
)

data class FollowShop(
    @SerializedName("buttonLabel")
    val buttonLabel: String = "",
    @SerializedName("isFirstTime")
    val isFirstTime: Boolean = false,
    @SerializedName("isFollowing")
    val isFollowing: Boolean = false,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("success")
    val success: Boolean = false,
)