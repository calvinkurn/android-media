package com.tokopedia.feedcomponent.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 04/12/18.
 */
data class Meta (

    @SerializedName("totalData")
    @Expose
    val totalData: Int = 0,
    @SerializedName("lastCursor")
    @Expose
    val lastCursor: String = "",
    @SerializedName("hasNextPage")
    @Expose
    val hasNextPage: Boolean = false,
    @SerializedName("linkSelf")
    @Expose
    val linkSelf: String = ""
)