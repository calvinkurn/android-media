package com.tokopedia.topads.data.param

import com.google.gson.annotations.SerializedName

open class Ad(
        @SerializedName("adType")
        var adType: String = "1",
        @SerializedName("adID")
        var adID: String = "0"
)