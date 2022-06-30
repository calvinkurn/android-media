package com.tokopedia.tokofood.feature.home.domain.data

import com.google.gson.annotations.SerializedName

data class TokoFoodParamFilterMerchant (
    @SerializedName("brandID")
    var brandUId: String = "",
    @SerializedName("option")
    var option: Int = 0,
    @SerializedName("cuisines")
    var cuisines: List<String> = emptyList()
)