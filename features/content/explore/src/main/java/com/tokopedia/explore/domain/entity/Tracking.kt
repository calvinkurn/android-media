package com.tokopedia.explore.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-10-17
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
        val source: String = "",

        @SerializedName("viewType")
        @Expose
        val viewType: String = "",

        @SerializedName("recomID")
        @Expose
        val recomID: Long = 0
)
