package com.tokopedia.kolcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.SerializedName

class Comment {
    /**
     * fmt : 100
     * value : 100
     */

    @SerializedName("fmt")
    var fmt: String? = ""
    @SerializedName("value")
    var value: Int = 0
}