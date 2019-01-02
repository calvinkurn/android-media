package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.SerializedName

data class FollowCta(
        @SerializedName("isFollow")
        val isFollow: Boolean = false,
        @SerializedName("textFalse")
        val textFalse: String = "",
        @SerializedName("textTrue")
        val textTrue: String = ""
)