package com.tokopedia.homenav.mainnav.data.pojo.shop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-10.
 */
data class ShopInfoPojo(
        @SerializedName("shopCore")
        val shopCore: ShopCore = ShopCore()
) {

    data class Response(
            @SerializedName("shopInfoByID")
            val result: Result = Result()
    )

    data class Result(
            @SerializedName("result")
            @Expose
            val data: List<ShopInfoPojo> = listOf(),
            @SerializedName("error")
            val error: Error = Error()
    )

    data class ShopCore(
            @SerializedName("name")
            val name: String = "",
            @SerializedName("shopID")
            val shopId: String = ""
    )

    data class Error(
            @SerializedName("message")
            val message: String = ""
    )
}