package com.tokopedia.flight.search.data.api.single.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Rizky on 23/10/18.
 */
class AttributesAirline(
        @SerializedName("name")
        @Expose
        val name: String,
        @SerializedName("short_name")
        @Expose
        val shortName: String,
        @SerializedName("logo")
        @Expose
        val logo: String,
        @SerializedName("city")
        @Expose
        val city: String
): AttributesInc()