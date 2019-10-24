package com.tokopedia.hotel.booking.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by resakemal on 15/05/19
 */

data class HotelCart (
        @SerializedName("cartID")
        @Expose
        val cartID: String = "",

        @SerializedName("cart")
        @Expose
        val cart: HotelCartData = HotelCartData(),

        @SerializedName("property")
        @Expose
        val property: HotelPropertyData = HotelPropertyData()
) {
        data class Response (
                @SerializedName("propertyGetCart")
                @Expose
                val response: HotelCart = HotelCart()
        )
}

