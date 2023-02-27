package com.tokopedia.feedplus.data

import com.google.gson.annotations.SerializedName

data class UserProfile(

    @SerializedName("isShown")
    var isShown: Boolean? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("weblink")
    var weblink: String? = null,
    @SerializedName("applink")
    var applink: String? = null,
    @SerializedName("__typename")
    var _typename: String? = null

)
