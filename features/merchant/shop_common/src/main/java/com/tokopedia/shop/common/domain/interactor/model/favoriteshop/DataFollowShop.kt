package com.tokopedia.shop.common.domain.interactor.model.favoriteshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataFollowShop {
    @SerializedName("followShop")
    @Expose
    var followShop: FollowShop? = null
}