package com.tokopedia.kolcomponent.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kolcomponent.data.pojo.feed.Feed

/**
 * @author by yfsx on 04/12/18.
 */
class FeedData {

    @SerializedName("meta")
    @Expose
    val meta: Meta? = null
    @SerializedName("included")
    @Expose
    val included: Included? = null
    @SerializedName("data")
    @Expose
    val data: List<Feed>? = ArrayList()
}