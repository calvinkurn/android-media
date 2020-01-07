package com.tokopedia.salam.umrah.search.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by M on 18/10/2019
 */
data class ParamFilter(
        @SerializedName("departurePeriod")
        @Expose
        var departurePeriod: String = "-",

        @SerializedName("departureCity")
        @Expose
        var departureCity: String = "-",

        @SerializedName("minPeriod")
        @Expose
        var durationDaysMinimum: Int = 0,

        @SerializedName("maxPeriod")
        @Expose
        var durationDaysMaximum: Int = 0,

        @SerializedName("minPrice")
        @Expose
        var priceMinimum: Int = 0,

        @SerializedName("maxPrice")
        @Expose
        var priceMaximum: Int = 0
)

