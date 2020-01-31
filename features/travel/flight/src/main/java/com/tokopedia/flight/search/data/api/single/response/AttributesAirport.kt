package com.tokopedia.flight.search.data.api.single.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Rizky on 29/10/18.
 */
class AttributesAirport(
        @SerializedName("name")
        @Expose
        val name: String,
        @SerializedName("city")
        @Expose
        val city: String
): AttributesInc()