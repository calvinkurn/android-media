package com.tokopedia.shop.common.graphql.data.shopinfo

import com.google.gson.annotations.SerializedName

data class BroadcasterConfig (
        @SerializedName("broadcasterGetShopConfig")
        val response: Response
){
    data class Response(
        @SerializedName("streamAllowed")
        val streamAllowed: Boolean = false
    )
}

