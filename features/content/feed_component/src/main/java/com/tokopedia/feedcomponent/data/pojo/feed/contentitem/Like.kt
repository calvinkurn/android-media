package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.SerializedName

data class Like (

    @SerializedName("fmt")
    var fmt: String = "",
    @SerializedName("value")
    var value: Int = 0,
    @SerializedName("checked")
    var isChecked: Boolean = false
)