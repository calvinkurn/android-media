package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 04/12/18.
 */
data class Item (

    @SerializedName("header")
    @Expose
    var header: Header = Header(),

    @SerializedName("media")
    @Expose
    var media: MutableList<Media> = ArrayList()
)