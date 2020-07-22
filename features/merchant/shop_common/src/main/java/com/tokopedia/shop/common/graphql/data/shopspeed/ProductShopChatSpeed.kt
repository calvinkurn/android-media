package com.tokopedia.shop.common.graphql.data.shopspeed


import com.google.gson.annotations.SerializedName

data class ProductShopChatSpeed(
        @SerializedName("ProductShopChatSpeedQuery")
        val response: ProductShopChatSpeedQuery = ProductShopChatSpeedQuery()
)

data class ProductShopChatSpeedQuery(
        //In minute, for "Chat dibalas +- 5 menit"
        @SerializedName("messageResponseTime")
        val messageResponseTime: Int = 0
)
