package com.tokopedia.shop.common.domain.interactor.model.favoriteshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FollowShop {
    @SerializedName("success")
    @Expose
    var isSuccess = false

    @SerializedName("message")
    @Expose
    var message: String? = null
}