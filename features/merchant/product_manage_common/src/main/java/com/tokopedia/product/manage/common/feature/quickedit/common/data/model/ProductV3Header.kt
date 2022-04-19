package com.tokopedia.product.manage.common.feature.quickedit.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductUpdateV3Header(
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