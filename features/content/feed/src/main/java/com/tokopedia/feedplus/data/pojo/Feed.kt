package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class Feed(
    @SerializedName("id")
    @Expose val id: String = "",

    @SerializedName("create_time")
    @Expose val createTime: String = "",

    @SerializedName("type")
    @Expose val type: String = "",

    @SerializedName("cursor")
    @Expose val cursor: String = "",

    @SerializedName("allow_report")
    @Expose val allowReport: Boolean = false,

    @SerializedName("source")
    @Expose val source: FeedSource = FeedSource(),

    @SerializedName("content")
    @Expose val content: FeedContent = FeedContent(),

    @SerializedName("meta")
    @Expose val meta: FeedMeta = FeedMeta()
)
