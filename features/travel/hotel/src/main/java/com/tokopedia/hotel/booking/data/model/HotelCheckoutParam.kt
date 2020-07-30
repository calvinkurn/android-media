package com.tokopedia.hotel.booking.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by resakemal on 15/05/19
 */

data class HotelCheckoutParam(
        @SerializedName("idempotencyKey")
        @Expose
        var idempotencyKey: String = "",

        @SerializedName("cartID")
        @Expose
        val cartId: String = "",

        @SerializedName("contact")
        @Expose
        val contact: HotelCartData.BookingContact = HotelCartData.BookingContact(),

        @SerializedName("guestName")
        @Expose
        val guestName: String = "",

        @SerializedName("promoCode")
        @Expose
        val promoCode: String = "",

        @SerializedName("specialRequest")
        @Expose
        val specialRequest: String = ""
)