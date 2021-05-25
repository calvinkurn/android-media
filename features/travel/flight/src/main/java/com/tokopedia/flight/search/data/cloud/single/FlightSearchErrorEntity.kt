package com.tokopedia.flight.search.data.cloud.single

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 06/04/2020
 */
class FlightSearchErrorEntity(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("title")
        @Expose
        val title: String = ""
)