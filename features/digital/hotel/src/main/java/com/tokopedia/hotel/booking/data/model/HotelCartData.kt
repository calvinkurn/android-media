package com.tokopedia.hotel.booking.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HotelCartData (

    @SerializedName("adult")
    @Expose
    val adult: Int = 0,

    @SerializedName("rooms")
    @Expose
    val rooms: List<HotelCartRoom> = listOf(),

    @SerializedName("checkIn")
    @Expose
    val checkIn: String = "",

    @SerializedName("checkOut")
    @Expose
    val checkOut: String = "",

    @SerializedName("guestName")
    @Expose
    val guestName: String = "",

    @SerializedName("specialRequest")
    @Expose
    val specialRequest: String = "",

    @SerializedName("totalPrice")
    @Expose
    val totalPrice: String = "",

    @SerializedName("totalPriceAmount")
    @Expose
    val totalPriceAmount: Float = 0f,

    @SerializedName("currency")
    @Expose
    val currency: String = "",

    @SerializedName("localTotalPrice")
    @Expose
    val localTotalPrice: String = "",

    @SerializedName("localTotalPriceAmount")
    @Expose
    val localTotalPriceAmount: Float = 0f,

    @SerializedName("localCurrency")
    @Expose
    val localCurrency: String = "",

    @SerializedName("fares")
    @Expose
    val fares: List<BookingFare> = listOf(),

    @SerializedName("contact")
    @Expose
    val contact: BookingContact = BookingContact()

) {

    data class BookingContact (

        @SerializedName("name")
        @Expose
        var name: String = "",

        @SerializedName("email")
        @Expose
        var email: String = "",

        @SerializedName("phone")
        @Expose
        var phone: String = "",

        @SerializedName("phoneCode")
        @Expose
        var phoneCode: Int = 0

    )

    data class HotelCartRoom (

        @SerializedName("roomID")
        @Expose
        var roomID: String = "",

        @SerializedName("numOfRooms")
        @Expose
        var numOfRooms: Int = 0

    )

    data class BookingFare (

        @SerializedName("type")
        @Expose
        var type: String = "",

        @SerializedName("description")
        @Expose
        var description: String = "",

        @SerializedName("priceAmount")
        @Expose
        var priceAmount: Float = 0f,

        @SerializedName("price")
        @Expose
        var price: String = "",

        @SerializedName("localPriceAmount")
        @Expose
        var localPriceAmount: Float = 0f,

        @SerializedName("localPrice")
        @Expose
        var localPrice: String = ""

    )

}