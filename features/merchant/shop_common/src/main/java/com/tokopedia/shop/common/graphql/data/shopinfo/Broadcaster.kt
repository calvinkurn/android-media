package com.tokopedia.shop.common.graphql.data.shopinfo

import com.google.gson.annotations.SerializedName

data class Broadcaster (
        @SerializedName("broadcasterGetShopConfig")
        val config: Config
){
    data class Config(
        @SerializedName("streamAllowed")
        val streamAllowed: Boolean = false
    )
}

