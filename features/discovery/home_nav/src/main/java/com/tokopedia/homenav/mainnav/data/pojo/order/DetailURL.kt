package com.tokopedia.homenav.mainnav.data.pojo.order

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DetailURL(
        @SerializedName("appTypeLink")
        @Expose
        val appTypeLink: String? = "",
        @SerializedName("appURL")
        @Expose
        val appURL: String? = "",
        @SerializedName("webTypeLink")
        @Expose
        val webTypeLink: String? = "",
        @SerializedName("webURL")
        @Expose
        val webURL: String? = ""
)