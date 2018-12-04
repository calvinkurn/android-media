package com.tokopedia.kolcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.SerializedName

class ButtonCta {

    @SerializedName("text")
    var text: String? = ""
    @SerializedName("appLink")
    var appLink: String? = ""
    @SerializedName("webLink")
    var webLink: String? = ""
}