package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostTagItemShop(
        @SerializedName("shopID")
        @Expose
        val shopId: String = ""
)