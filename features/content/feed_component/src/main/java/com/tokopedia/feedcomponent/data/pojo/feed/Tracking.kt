package com.tokopedia.feedcomponent.data.pojo.feed

import com.google.gson.annotations.SerializedName

data class Tracking (
    @SerializedName("id")
    var id: String = "",
    @SerializedName("type")
    var type: String = ""
)