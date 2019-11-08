package com.tokopedia.flight.bookingV3.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-11-08
 */

data class FlightVerifyParam(
        @SerializedName("cartItems")
        @Expose
        val cartItems: List<CartItem> = listOf(),

        @SerializedName("promoCode")
        @Expose
        val promoCode: String = ""
) {
    data class CartItem(
            @SerializedName("productID")
            @Expose
            val productId: Int = 0,

            @SerializedName("quantity")
            @Expose
            val quantity: Int = 0,

            @SerializedName("metaData")
            @Expose
            val metaData: MetaData = MetaData(),

            @SerializedName("configuration")
            @Expose
            val configuration: FlightVerify.CartConfiguration = FlightVerify.CartConfiguration()
    )

    data class MetaData(
            @SerializedName("cartID")
            @Expose
            val cartId: String = "",

            @SerializedName("contactName")
            @Expose
            val contactName: String = "",

            @SerializedName("email")
            @Expose
            val email: String = "",

            @SerializedName("phone")
            @Expose
            val phone: String = "",

            @SerializedName("country")
            @Expose
            val country: String = "",

            @SerializedName("ipAddress")
            @Expose
            val ipAddress: String = "",

            @SerializedName("userAgent")
            @Expose
            val userAgent: String = "",

            @SerializedName("passengers")
            @Expose
            val passengers: List<FlightCart.Passenger> = listOf(),

            @SerializedName("insurances")
            @Expose
            val insurances: List<String> = listOf()
    )

        data class Passenger(
                @SerializedName("type")
                @Expose
                val type: Int = 0,

                @SerializedName("title")
                @Expose
                val title: Int = 0,

                @SerializedName("firstName")
                @Expose
                val firstName: String = "",

                @SerializedName("lastName")
                @Expose
                val lastName: String = "",

                @SerializedName("dob")
                @Expose
                val dob: String = "",

                @SerializedName("nationality")
                @Expose
                val nationality: String = "",

                @SerializedName("passportNumber")
                @Expose
                val passportNumber: String = "",

                @SerializedName("passportCountry")
                @Expose
                val passportCountry: String = "",

                @SerializedName("passportExpire")
                @Expose
                val passportExpire: String = "",

                @SerializedName("amenities")
                @Expose
                val amenities: List<Amenity> = listOf()
        )

        data class Amenity(
                @SerializedName("journeyID")
                @Expose
                val journeyId: String = "",

                @SerializedName("departureAirportID")
                @Expose
                val departureAirportId: String = "",

                @SerializedName("arrivalAirportID")
                @Expose
                val arrivalAirportId: String = "",

                @SerializedName("type")
                @Expose
                val type: Int = 0,

                @SerializedName("key")
                @Expose
                val key: String = "",

                @SerializedName("itemID")
                @Expose
                val itemId: String = ""
        )
}