package com.tokopedia.flight.bookingV3.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-11-13
 */

data class FlightAddToCartData(
        @SerializedName("id")
        @Expose
        val id: String = ""
) {
    data class Response(
            @SerializedName("flightAddToCart")
            @Expose
            val addToCartData: FlightAddToCartData = FlightAddToCartData()
    )

}