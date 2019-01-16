package com.tokopedia.feedcomponent.data.pojo.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 04/12/18.
 */
data class Feed (
    @SerializedName("id")
    @Expose
    val id: Int = 0,
    @SerializedName("createTime")
    @Expose
    val createTime: String = "",
    @SerializedName("type")
    @Expose
    val type: String = "",
    @SerializedName("activity")
    @Expose
    val activity: String = "",
    @SerializedName("content")
    @Expose
    val content: Content = Content(),
    @SerializedName("tracking")
    @Expose
    val tracking: Tracking = Tracking(),
    @SerializedName("template")
    @Expose
    val template: Int = 0

)