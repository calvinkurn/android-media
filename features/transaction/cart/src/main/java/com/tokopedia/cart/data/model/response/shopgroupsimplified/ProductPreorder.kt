package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ProductPreorder(
    @SerializedName("duration_day")
    val durationDay: Int = 0
)
