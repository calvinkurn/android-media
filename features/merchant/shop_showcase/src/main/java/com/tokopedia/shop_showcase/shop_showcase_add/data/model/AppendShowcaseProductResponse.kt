package com.tokopedia.shop_showcase.shop_showcase_add.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppendShowcaseProductBaseResponse(
        @Expose
        @SerializedName("AppendProductMenu")
        val appendProductMenuResponse: AppendShowcaseProductResponse = AppendShowcaseProductResponse()
)

data class AppendShowcaseProductResponse(
        @Expose
        @SerializedName("header") val header: AppendShowcaseProductResponseHeader = AppendShowcaseProductResponseHeader(),
        @Expose
        @SerializedName("isSuccess") val status: Boolean = false
)

data class AppendShowcaseProductResponseHeader(
        @Expose
        @SerializedName("processTime") val processTime: Double = 0.0,
        @Expose
        @SerializedName("errorCode") val errorCode: String = "",
        @Expose
        @SerializedName("messages") val messages: ArrayList<String> = arrayListOf(),
        @Expose
        @SerializedName("reason") val reason: String = ""
)