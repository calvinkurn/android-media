package com.tokopedia.product.manage.common.feature.getstatusshop.data.model

import com.google.gson.annotations.SerializedName

data class ShopInfoById(
    @SerializedName("result")
    val result: List<ShopInfo>
)
