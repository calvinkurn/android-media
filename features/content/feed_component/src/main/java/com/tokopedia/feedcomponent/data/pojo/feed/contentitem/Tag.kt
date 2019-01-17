package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.SerializedName

data class Tag(
        @SerializedName("linkType")
        val linkType: String = ""
)