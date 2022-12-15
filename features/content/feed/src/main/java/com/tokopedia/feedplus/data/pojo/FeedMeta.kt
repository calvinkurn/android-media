package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class FeedMeta(
    @SerializedName("has_next_page")
    @Expose val hasNextPage: Boolean = false
)
