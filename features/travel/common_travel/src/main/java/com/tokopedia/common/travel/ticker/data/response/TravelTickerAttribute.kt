package com.tokopedia.common.travel.ticker.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 18/02/19
 */
class TravelTickerAttribute(
        @SerializedName("Title")
        @Expose
        val title: String = "",
        @SerializedName("Message")
        @Expose
        val message: String = "",
        @SerializedName("URL")
        @Expose
        val url: String = "",
        @SerializedName("Type")
        @Expose
        val type: Int = 0,
        @SerializedName("Status")
        @Expose
        val status: Int = 0,
        @SerializedName("EndTime")
        @Expose
        val endTime: String = "",
        @SerializedName("StartTime")
        @Expose
        val startTime: String = "",
        @SerializedName("Instances")
        @Expose
        val instances: Int = 0,
        @SerializedName("Device")
        @Expose
        val device: Int = 0,
        @SerializedName("Page")
        @Expose
        val page: String = "",
        @SerializedName("IsPeriod")
        @Expose
        val isPeriod: Boolean = false)