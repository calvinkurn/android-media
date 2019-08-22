package com.tokopedia.product.manage.list.data.model.mutationeditproduct

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductUpdateV3Header(
        @SerializedName("message")
        @Expose
        val errorMessage: List<String> = listOf(),
        @SerializedName("reason")
        @Expose
        val reason: String = "",
        @SerializedName("errorCode")
        @Expose
        val errorCode: String = ""
)
