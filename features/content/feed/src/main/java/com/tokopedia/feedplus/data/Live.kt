package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

data class Live(

    @SerializedName("isActive")
    var isActive: Boolean? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("weblink")
    var weblink: String? = null,
    @SerializedName("applink")
    var applink: String? = null,
    @SerializedName("__typename")
    var _typename: String? = null

)
