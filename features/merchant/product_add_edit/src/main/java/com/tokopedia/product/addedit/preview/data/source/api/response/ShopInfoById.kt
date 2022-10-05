package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class ShopInfoById(
    @SerializedName("result")
    val result: List<ShopInfo>
)
