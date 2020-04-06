package com.tokopedia.flight.searchV4.data.cloud.single

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 06/04/2020
 */
class FlightSearchErrorEntity(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("title")
        @Expose
        val title: String = ""
)