package com.tokopedia.tokofood.feature.home.domain.data

import com.google.gson.annotations.SerializedName

data class TokoFoodParamSort(
    @SerializedName("sortBy")
    var sortBy: Int = 0,
    @SerializedName("orderBy")
    var orderBy: Int = 0
)