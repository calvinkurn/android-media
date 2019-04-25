package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopInfoModerate(

        @SerializedName("data")
        @Expose
        val data: Data = Data(),

        @SerializedName("errors")
        @Expose
        val errors: List<Error> = listOf()

)

data class Data(
        @SerializedName("moderateShop")
        @Expose
        val moderateShop: ModerateShop = ModerateShop()
)


data class Error(
        @SerializedName("message")
        @Expose

        val message: String = "",

        @SerializedName("path")
        @Expose
        val path: List<String> = listOf()
)

data class ModerateShop(

        @SerializedName("success")
        @Expose
        val success: Boolean = false,

        @SerializedName("message")
        @Expose
        val message: String = ""

)