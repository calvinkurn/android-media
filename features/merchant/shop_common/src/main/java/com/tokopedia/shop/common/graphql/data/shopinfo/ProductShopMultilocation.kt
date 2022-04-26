package com.tokopedia.shop.common.graphql.data.shopinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductShopMultilocation(
        @SerializedName("warehouseCount")
        @Expose
        val warehouseCount: String = "",

        @SerializedName("applink")
        @Expose
        val applink: String = ""
)
