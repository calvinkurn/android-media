package com.tokopedia.feedcomponent.data.pojo.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Body
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Title

data class Cardbanner(
        @SerializedName("title")
        @Expose
        val title: Title = Title(),

        @SerializedName("body")
        @Expose
        val body: Body = Body()
)