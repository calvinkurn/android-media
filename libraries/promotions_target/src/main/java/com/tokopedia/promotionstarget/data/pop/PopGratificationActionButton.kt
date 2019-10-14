package com.tokopedia.promotionstarget.data.pop

import com.google.gson.annotations.SerializedName

data class PopGratificationActionButton(

        @SerializedName("appLink")
        val appLink: String? = null,

        @SerializedName("text")
        val text: String? = null,

        @SerializedName("type")
        val type: String? = null,

        @SerializedName("url")
        val url: String? = null
)