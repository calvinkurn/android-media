package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class FreeReturns(
    @SerializedName("free_returns_logo")
    var freeReturnsLogo: String = ""
)
