package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopInfoByIDResponse(
        @Expose
        @SerializedName("shopInfoByID")
        val shopInfoByID: ShopInfoByID = ShopInfoByID()
) {
    data class ShopInfoByID(
            @Expose
            @SerializedName("error")
            val error: Error = Error(),
            @Expose
            @SerializedName("result")
            val result: List<Result> = listOf()
    ) {
        data class Result(
                @Expose
                @SerializedName("createInfo")
                val createInfo: CreateInfo = CreateInfo()
        ) {
            data class CreateInfo(
                    @Expose
                    @SerializedName("shopCreated")
                    val shopCreated: String = ""
            )
        }

        data class Error(
                @Expose
                @SerializedName("message")
                val message: String = ""
        )
    }
}