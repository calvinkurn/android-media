package com.tokopedia.hotel.booking.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HotelPropertyData {

    @SerializedName("propertyID")
    @Expose
    val propertyID: Int = 0

    @SerializedName("name")
    @Expose
    val name: String = ""

    @SerializedName("type")
    @Expose
    val type: String = ""

    @SerializedName("address")
    @Expose
    val address: String = ""

    @SerializedName("image")
    @Expose
    val image: String = ""

    @SerializedName("star")
    @Expose
    val star: Int = 0

    @SerializedName("paymentNote")
    @Expose
    val paymentNote: String = ""

    @SerializedName("rooms")
    @Expose
    val rooms: List<HotelPropertyRoom> = listOf()

}
