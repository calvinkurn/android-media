package com.tokopedia.hotel.booking.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyImageItem

class HotelPropertyData {

    @SerializedName("propertyID")
    @Expose
    val propertyID: Long = 0

    @SerializedName("name")
    @Expose
    val name: String = ""

    @SerializedName("type")
    @Expose
    val type: String = ""

    @SerializedName("address")
    @Expose
    val address: String = ""

    @SerializedName("propertyImage")
    @Expose
    val image: PropertyImageItem = PropertyImageItem()

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

    @SerializedName("isDirectPaymentString")
    @Expose
    val isDirectPaymentString: String = ""
}
