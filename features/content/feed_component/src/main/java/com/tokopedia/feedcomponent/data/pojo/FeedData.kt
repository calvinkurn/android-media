package com.tokopedia.feedcomponent.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.feedcomponent.data.pojo.feed.Feed

/**
 * @author by yfsx on 04/12/18.
 */
data class FeedData (

    @SerializedName("meta")
    @Expose
    val meta: Meta = Meta(),
    @SerializedName("included")
    @Expose
    val included: Included = Included(),
    @SerializedName("data")
    @Expose
    val data: List<Feed> = ArrayList()
)