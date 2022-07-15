package com.tokopedia.shop.common.graphql.data.shopinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductShopMultilocation(
        @SerializedName("warehouseCount")
        @Expose
        val warehouseCount: String = "",

        @SerializedName("eduLink")
        @Expose
        val eduLink: ShopEduLink = ShopEduLink()
)

data class ShopEduLink(
        @SerializedName("appLink")
        @Expose
        val applink: String = ""
)
