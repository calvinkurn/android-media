package com.tokopedia.hotel.roomlist.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 08/05/19
 */

data class HotelAddCartData(
        @SerializedName("cartID")
        @Expose
        val cartId: String = ""
) {
        data class Response(
                @SerializedName("propertyAddToCart")
                @Expose
                val response: HotelAddCartData = HotelAddCartData()
        )
}