package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 20/03/19.
 */
data class Video(
        @SerializedName("url")
        @Expose
        var url: String = ""
) {
}