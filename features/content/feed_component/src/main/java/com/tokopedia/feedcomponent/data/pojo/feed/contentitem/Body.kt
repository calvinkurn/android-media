package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Body(
        @SerializedName("caption")
        @Expose
        var caption: Caption = Caption(),
        @SerializedName("media")
        @Expose
        var media: List<Media> = ArrayList()

)