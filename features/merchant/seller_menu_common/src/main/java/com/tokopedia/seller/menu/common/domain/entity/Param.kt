package com.tokopedia.seller.menu.common.domain.entity

import com.google.gson.annotations.SerializedName

data class Param(
    @SerializedName("shop_id")
    val shopId: String
)