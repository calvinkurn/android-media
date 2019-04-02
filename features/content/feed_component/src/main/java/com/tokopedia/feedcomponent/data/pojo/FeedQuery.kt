package com.tokopedia.feedcomponent.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 04/12/18.
 */
data class FeedQuery (
    @SerializedName("feedv2")
    @Expose
    val feedv2: FeedData = FeedData()
)