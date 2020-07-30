package com.tokopedia.find_native.data.model

import com.google.gson.annotations.SerializedName

data class RelatedLinkData(
        @SerializedName("id") val id : Int,
        @SerializedName("url") val url : String,
        @SerializedName("text") val text : String
)