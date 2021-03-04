package com.tokopedia.common.travel.ticker.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 23/11/20
 */

data class TravelTickerRequest(
        @SerializedName("page")
        @Expose
        val page: String,

        @SerializedName("instance")
        @Expose
        val instance: String,

        @SerializedName("deviceID")
        @Expose
        val deviceID:  Int
)