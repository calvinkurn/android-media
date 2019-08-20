package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostTagItemButtonCTA(
        @SerializedName("text")
        @Expose
        val text: String = "",

        @SerializedName("textDisabled")
        @Expose
        val textDisabled: String = "",

        @SerializedName("isDisabled")
        @Expose
        val isDisabled: Boolean = true
)