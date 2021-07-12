package com.tokopedia.flight.search.data.cloud.single

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 06/04/2020
 */
class FlightSearchMetaEntity(
        @SerializedName("needRefresh")
        @Expose
        val needRefresh: Boolean = false,
        @SerializedName("refreshTime")
        @Expose
        val refreshTime: Int = 0,
        @SerializedName("maxRetry")
        @Expose
        val maxRetry: Int = 0,
        @SerializedName("adult")
        @Expose
        val adult: Int = 1,
        @SerializedName("child")
        @Expose
        val child: Int = 0,
        @SerializedName("infant")
        @Expose
        val infant: Int = 0,
        @SerializedName("requestID")
        @Expose
        val requestId: String = "",
        @SerializedName("internationalTransitTag")
        @Expose
        val internationalTag: String = "",
        @SerializedName("backgroundRefreshTime")
        @Expose
        val backgroundRefreshTime: Int = 0
) {
    var airlineList: List<String> = arrayListOf()
}