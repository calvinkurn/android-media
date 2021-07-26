package com.tokopedia.shop.common.domain.interactor.model.favoriteshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop

class ResponseFollowShop {
    @SerializedName("data")
    @Expose
    var dataFollowShop: DataFollowShop? = null
}