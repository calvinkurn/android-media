package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-11-21
 */
data class Stats(
        @SerializedName("text")
        @Expose
        val text: String = "",

        @SerializedName("productIDs")
        @Expose
        val productIDs: List<String> = emptyList()
)