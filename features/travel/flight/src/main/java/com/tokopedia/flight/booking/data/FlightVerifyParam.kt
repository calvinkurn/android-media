package com.tokopedia.flight.booking.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-11-08
 */

data class FlightVerifyParam(
        @SerializedName("cartItems")
        @Expose
        var cartItems: MutableList<CartItem> = mutableListOf(),

        @SerializedName("promoCode")
        @Expose
        var promoCode: String = ""
) {
    data class CartItem(
            @SerializedName("productID")
            @Expose
            var productId: Int = 0,

            @SerializedName("quantity")
            @Expose
            var quantity: Int = 0,

            @SerializedName("metaData")
            @Expose
            var metaData: MetaData = MetaData(),

            @SerializedName("configuration")
            @Expose
            var configuration: FlightVerify.CartConfiguration = FlightVerify.CartConfiguration()
    )

    data class MetaData(
            @SerializedName("cartID")
            @Expose
            var cartId: String = "",

            @SerializedName("contactName")
            @Expose
            var contactName: String = "",

            @SerializedName("email")
            @Expose
            var email: String = "",

            @SerializedName("phone")
            @Expose
            var phone: String = "",

            @SerializedName("country")
            @Expose
            var country: String = "",

            @SerializedName("ipAddress")
            @Expose
            var ipAddress: String = "",

            @SerializedName("userAgent")
            @Expose
            var userAgent: String = "",

            @SerializedName("passengers")
            @Expose
            var passengers: MutableList<Passenger> = mutableListOf(),

            @SerializedName("insurances")
            @Expose
            var insurances: MutableList<String> = mutableListOf()
    )

        data class Passenger(
                @SerializedName("type")
                @Expose
                var type: Int = 0,

                @SerializedName("title")
                @Expose
                var title: Int = 0,

                @SerializedName("firstName")
                @Expose
                var firstName: String = "",

                @SerializedName("lastName")
                @Expose
                var lastName: String = "",

                @SerializedName("dob")
                @Expose
                var dob: String = "",

                @SerializedName("nationality")
                @Expose
                var nationality: String = "",

                @SerializedName("passportNumber")
                @Expose
                var passportNumber: String = "",

                @SerializedName("passportCountry")
                @Expose
                var passportCountry: String = "",

                @SerializedName("passportExpire")
                @Expose
                var passportExpire: String = "",

                @SerializedName("amenities")
                @Expose
                var amenities: MutableList<Amenity> = mutableListOf()
        )

        data class Amenity(
                @SerializedName("journeyID")
                @Expose
                var journeyId: String = "",

                @SerializedName("departureAirportID")
                @Expose
                var departureAirportId: String = "",

                @SerializedName("arrivalAirportID")
                @Expose
                var arrivalAirportId: String = "",

                @SerializedName("type")
                @Expose
                var type: Int = 0,

                @SerializedName("key")
                @Expose
                var key: String = "",

                @SerializedName("itemID")
                @Expose
                var itemId: String = ""
        )
}