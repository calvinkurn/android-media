package com.tokopedia.shop.common.data.source.cloud.model.followshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FollowShopResponse (
    @SerializedName("followShop")
    @Expose
    val followShop: FollowShop?
)