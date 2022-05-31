package com.tokopedia.tokofood.home.domain.data

import com.google.gson.annotations.SerializedName

data class TokoFoodParamFilterMerchant (
    @SerializedName("brandID")
    var brandId: String = "",
    @SerializedName("option")
    var option: Int = 0
)