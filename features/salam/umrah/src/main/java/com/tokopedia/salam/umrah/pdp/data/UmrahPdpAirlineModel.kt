package com.tokopedia.salam.umrah.pdp.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.salam.umrah.search.data.City
/**
 * @author by M on 30/10/19
 */
data class UmrahPdpAirlineModel(
        @SerializedName("name")
        var name: String = "",

        @SerializedName("date")
        var date: String = "",

        @SerializedName("departureCity")
        var departureCity: City = City(),

        @SerializedName("arrivalCity")
        var arrivalCity: City = City(),

        @SerializedName("logoUrl")
        val logoUrl: String = ""
)