package com.tokopedia.shop.common.graphql.data.shopinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopCommitment(
        @SerializedName("isNowActive")
        @Expose
        val isNowActive: Boolean = false,

        @SerializedName("staticMessages")
        @Expose
        val staticMessages: StaticMessages = StaticMessages(),

        @SerializedName("iconURL")
        @Expose
        val iconUrl: String = ""
){

    data class StaticMessages(
            @SerializedName("pdpMessage")
            @Expose
            val pdpMessage: String = ""
    )

    data class Result(
            @SerializedName("result")
            @Expose
            val shopCommitment: ShopCommitment = ShopCommitment(),

            @SerializedName("error")
            @Expose
            val error: ShopError = ShopError()
    )

    data class Response(
            @SerializedName("shopCommitment")
            @Expose
            val result: Result = Result()
    )
}