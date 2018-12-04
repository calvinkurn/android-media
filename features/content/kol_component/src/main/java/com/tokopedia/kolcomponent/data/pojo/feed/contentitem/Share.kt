package com.tokopedia.kolcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Share {

    @SerializedName("text")
    @Expose
    var text: String = ""
}