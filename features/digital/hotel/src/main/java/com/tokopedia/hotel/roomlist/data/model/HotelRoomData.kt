package com.tokopedia.hotel.roomlist.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 15/04/19
 */

data class HotelRoomData(
        @SerializedName("propertyID")
        @Expose
        val propertyId: String,

        @SerializedName("rooms")
        @Expose
        val rooms: List<HotelRoom>,

        @SerializedName("isAddressRequired")
        @Expose
        val isAddressRequired: Boolean,

        @SerializedName("isCvCRequired")
        @Expose
        val isCvCRequired: Boolean,

        @SerializedName("isDirectPayment")
        @Expose
        val isDirectPayment: Boolean
) {
        data class Response(
                @SerializedName("propertySearchRoom")
                @Expose
                val response: HotelRoomData
        )
}

