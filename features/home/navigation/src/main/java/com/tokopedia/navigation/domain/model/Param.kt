package com.tokopedia.navigation.domain.model

import com.google.gson.annotations.SerializedName

data class Param(
    @SerializedName("shop_id")
    var shopId: String
)