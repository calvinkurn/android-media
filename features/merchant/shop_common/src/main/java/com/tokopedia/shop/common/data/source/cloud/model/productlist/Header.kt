package com.tokopedia.shop.common.data.source.cloud.model.productlist

import com.google.gson.annotations.SerializedName

data class Header(
    @SerializedName("processTime")
    val processTime: Float? = null,
    @SerializedName("messages")
    val messages: List<Any>? = null,
    @SerializedName("reason")
    val reason: String? = null,
    @SerializedName("errorCode")
    val errorCode: String? = null
)