package com.tokopedia.discovery.find.data.model

import com.google.gson.annotations.SerializedName

data class RelatedLinkData(
        @SerializedName("id") var id : Int,
        @SerializedName("url") var url : String,
        @SerializedName("text") var text : String
)