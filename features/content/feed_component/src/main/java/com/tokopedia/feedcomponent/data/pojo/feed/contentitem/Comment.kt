package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.SerializedName

data class Comment (

    @SerializedName("fmt")
    var fmt: String = "",
    @SerializedName("value")
    var value: Int = 0
)