package com.tokopedia.shop.common.graphql.data.shopspeed


import com.google.gson.annotations.SerializedName

data class ProductShopSpeed(
        @SerializedName("ProductShopPackSpeedQuery")
        val response: ProductShopPackSpeedQuery = ProductShopPackSpeedQuery()
)

data class ProductShopPackSpeedQuery(
        @SerializedName("hour")
        val hour: Long = 0,

        // Pesanan di proses dalam 2 jam
        @SerializedName("speedFmt")
        val speedFmt: String = ""
)
