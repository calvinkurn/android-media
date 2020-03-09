package com.tokopedia.product.manage.feature.quickedit.delete.data.model

import com.google.gson.annotations.SerializedName

data class DeleteProductHeader(
        @SerializedName("processTime")
        val processTime: Float = 0.0F,
        @SerializedName("errorCode")
        val errorCode: String = "",
        @SerializedName("messages")
        val messages: List<String> = listOf(),
        @SerializedName("reason")
        val reason: String = ""

)