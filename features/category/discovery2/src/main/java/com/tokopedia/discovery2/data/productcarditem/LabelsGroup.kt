package com.tokopedia.discovery2.data.productcarditem


import com.google.gson.annotations.SerializedName

data class LabelsGroup(
        @SerializedName("position")
        var position: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("type")
        val type: String = "",

        @SerializedName("url")
        val url: String = ""
)