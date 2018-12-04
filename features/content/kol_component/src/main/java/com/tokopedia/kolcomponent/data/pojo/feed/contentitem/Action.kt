package com.tokopedia.kolcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Action {

    @SerializedName("event")
    @Expose
    var event: String? = ""
    @SerializedName("action")
    @Expose
    var action: String? = ""
    @SerializedName("appLink")
    @Expose
    var appLink: String? = ""
    @SerializedName("webLink")
    @Expose
    var webLink: String? = ""
}