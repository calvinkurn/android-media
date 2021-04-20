package com.tokopedia.product.manage.common.feature.list.data.model.filter

import com.google.gson.annotations.SerializedName

data class ProductListMetaHeader(
        @SerializedName("processTime")
        val processTime: Float = 0.0F,
        @SerializedName("messages")
        val messages: List<String> = listOf(),
        @SerializedName("reason")
        val reason: String = "",
        @SerializedName("errorCode")
        val errorCode: String = ""
)