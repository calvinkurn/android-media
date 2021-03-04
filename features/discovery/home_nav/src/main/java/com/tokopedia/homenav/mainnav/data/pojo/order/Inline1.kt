package com.tokopedia.homenav.mainnav.data.pojo.order

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Inline1(
        @SerializedName("bgColor")
        @Expose
        val bgColor: String? = "",
        @SerializedName("label")
        @Expose
        val label: String? = "",
        @SerializedName("textColor")
        @Expose
        val textColor: String? = ""
)