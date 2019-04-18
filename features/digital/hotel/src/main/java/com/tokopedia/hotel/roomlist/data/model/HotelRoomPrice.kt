package com.tokopedia.hotel.roomlist.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 15/04/19
 */

data class HotelRoomPrice(

    @SerializedName("price")
    @Expose
    val roomPrice: String,

    @SerializedName("priceAmount")
    @Expose
    val priceAmount: Double,

    @SerializedName("totalPrice")
    @Expose
    val totalPrice: String,

    @SerializedName("totalPriceAmount")
    @Expose
    val totalPriceAmount: Double,

    @SerializedName("extraCharges")
    @Expose
    val extraCharges: ExtraCharges

) {
    data class ExtraCharges (
        @SerializedName("netPrice")
        @Expose
        val netPrice: String,

        @SerializedName("netPriceAmount")
        @Expose
        val netPriceAmount: Double,

        @SerializedName("extraChargeInfo")
        @Expose
        val extraChargeInfo: List<ExtraChargeInfo>

    ) {
        data class ExtraChargeInfo(
                @SerializedName("name")
                @Expose
                val name: String,

                @SerializedName("excluded")
                @Expose
                val excluded: Boolean,

                @SerializedName("type")
                @Expose
                val type: String,

                @SerializedName("price")
                @Expose
                val price: String,

                @SerializedName("priceAmount")
                @Expose
                val priceAmount: Double

        )
    }
}