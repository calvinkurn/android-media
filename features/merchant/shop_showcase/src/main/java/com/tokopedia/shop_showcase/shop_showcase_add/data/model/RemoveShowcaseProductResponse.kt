package com.tokopedia.shop_showcase.shop_showcase_add.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by Rafli Syam
 */
data class RemoveShowcaseProductBaseResponse(
        @Expose
        @SerializedName("RemoveProductMenu")
        val removeProductMenuResponse: RemoveShowcaseProductResponse = RemoveShowcaseProductResponse()
)

data class RemoveShowcaseProductResponse(
        @Expose
        @SerializedName("header") val header: RemoveShowcaseProductResponseHeader = RemoveShowcaseProductResponseHeader(),
        @Expose
        @SerializedName("isSuccess") val status: Boolean = false
)

data class RemoveShowcaseProductResponseHeader(
        @Expose
        @SerializedName("processTime") val processTime: Double = 0.0,
        @Expose
        @SerializedName("errorCode") val errorCode: String = "",
        @Expose
        @SerializedName("messages") val messages: ArrayList<String> = arrayListOf(),
        @Expose
        @SerializedName("reason") val reason: String = ""
)