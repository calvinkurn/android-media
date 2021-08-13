package com.tokopedia.sellerorder.list.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Error(
        @SerializedName("code")
        @Expose
        val code: String = "",
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("detail")
        @Expose
        val detail: String = ""
)