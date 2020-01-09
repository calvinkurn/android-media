package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.SerializedName

data class ButtonCta (

    @SerializedName("text")
    var text: String = "",
    @SerializedName("appLink")
    var appLink: String = "",
    @SerializedName("webLink")
    var webLink: String = ""
) {
    fun copy(): ButtonCta {
        return ButtonCta(text, appLink, webLink)
    }
}