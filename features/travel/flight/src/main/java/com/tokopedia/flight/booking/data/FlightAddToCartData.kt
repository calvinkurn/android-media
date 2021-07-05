package com.tokopedia.flight.booking.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-11-13
 */

data class FlightAddToCartData(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("meta")
        @Expose
        val meta: Meta = Meta()
) {
    data class Response(
            @SerializedName("flightAddToCart")
            @Expose
            val addToCartData: FlightAddToCartData = FlightAddToCartData()
    )
}

data class Meta(
        @SerializedName("requestID")
        @Expose
        val requestId: String = ""
)