package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.SerializedName

data class Share (

        @SerializedName("description")
        val description: String = "",
        @SerializedName("imageURL")
        val imageUrl: String = "",
        @SerializedName("text")
        val text: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("url")
        val url: String = ""
)