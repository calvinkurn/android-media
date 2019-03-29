package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Caption (

    @SerializedName("text")
    @Expose
    var text: String = "",
    @SerializedName("buttonName")
    @Expose
    var buttonName: String = "",
    @SerializedName("appLink")
    @Expose
    var appLink: String = "",
    @SerializedName("webLink")
    @Expose
    var webLink: String = ""
)