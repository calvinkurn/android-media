package com.tokopedia.hotel.roomlist.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 15/04/19
 */

data class HotelRoomData(
        @SerializedName("propertyID")
        @Expose
        val propertyId: Int = 0,

        @SerializedName("rooms")
        @Expose
        val rooms: List<HotelRoom> = listOf(),

        @SerializedName("isAddressRequired")
        @Expose
        val isAddressRequired: Boolean = false,

        @SerializedName("isCvCRequired")
        @Expose
        val isCvCRequired: Boolean = false,

        @SerializedName("isDirectPayment")
        @Expose
        val isDirectPayment: Boolean = false,

        @SerializedName("isEnabled")
        @Expose
        val isEnabled: Boolean = false,

        @SerializedName("deals")
        @Expose
        val deals: PriceDeals = PriceDeals()
) {
    data class Response(
            @SerializedName("propertySearchRoom")
            @Expose
            val response: HotelRoomData = HotelRoomData()
    )

    data class PriceDeals(
            @SerializedName("tagging")
            @Expose
            val tagging: String = "",

            @SerializedName("price")
            @Expose
            val price: String = "",

            @SerializedName("priceAmount")
            @Expose
            val priceAmount: Int = 0
    )
}

