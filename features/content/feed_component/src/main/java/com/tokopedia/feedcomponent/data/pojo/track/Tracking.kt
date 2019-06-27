package com.tokopedia.feedcomponent.data.pojo.track

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 15/03/19.
 */
data class Tracking(
        @SerializedName("clickURL")
        @Expose
        val clickURL: String = "",
        @SerializedName("viewURL")
        @Expose
        val viewURL: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("source")
        @Expose
        val source: String = ""
)