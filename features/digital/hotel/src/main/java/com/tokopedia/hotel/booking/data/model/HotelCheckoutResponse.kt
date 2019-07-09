package com.tokopedia.hotel.booking.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by resakemal on 15/05/19
 */

data class HotelCheckoutResponse(
        @SerializedName("queryString")
        @Expose
        var queryString: String = "",

        @SerializedName("redirectUrl")
        @Expose
        var redirectUrl: String = ""
) {
    class Response(
            @SerializedName("propertyCheckout")
            @Expose
            var response: HotelCheckoutResponse = HotelCheckoutResponse()
    )
}