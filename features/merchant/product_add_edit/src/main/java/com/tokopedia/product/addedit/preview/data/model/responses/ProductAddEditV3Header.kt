package com.tokopedia.product.addedit.preview.data.model.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductAddEditV3Header(
        @SerializedName("messages")
        @Expose
        val errorMessage: ArrayList<String> = arrayListOf(),
        @SerializedName("reason")
        @Expose
        val reason: String = "",
        @SerializedName("errorCode")
        @Expose
        val errorCode: String = ""
)
