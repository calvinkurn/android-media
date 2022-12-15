package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class Feeds(
    @SerializedName("links")
    @Expose val links: Links = Links(),

    @SerializedName("meta")
    @Expose val meta: FeedTotalData = FeedTotalData(),

    @SerializedName("data")
    @Expose val data: List<Feed> = emptyList(),

    @SerializedName("token")
    @Expose val token: String = ""
)
