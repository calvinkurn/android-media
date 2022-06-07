package com.tokopedia.shop.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class ShopInfoByIdRequest(
    @SerializedName("shopIDs")
    val shopIds: List<Long>,
    @SerializedName("fields")
    val fields: List<String>
)
