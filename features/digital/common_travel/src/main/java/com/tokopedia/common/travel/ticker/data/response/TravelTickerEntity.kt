package com.tokopedia.common.travel.ticker.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 18/02/19
 */
class TravelTickerEntity(
        @SerializedName("travelTicker")
        @Expose
        val travelTicker: TravelTickerAttribute)