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

    @SerializedName("checkInFrom")
    @Expose
    val checkInFrom: String = ""

    @SerializedName("checkInTo")
    @Expose
    val checkInTo: String = ""

    @SerializedName("checkOutFrom")
    @Expose
    val checkOutFrom: String = ""

    @SerializedName("checkOutTo")
    @Expose
    val checkOutTo: String = ""

    @SerializedName("isDirectPayment")
    @Expose
    val isDirectPayment: Boolean = false

    @SerializedName("room")
    @Expose
    val rooms: List<HotelPropertyRoom> = listOf()

}
