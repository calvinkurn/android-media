package com.tokopedia.hotel.roomlist.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 15/04/19
 */

data class HotelRoomPrice(

        @SerializedName("price")
        @Expose
        val roomPrice: String = "",

        @SerializedName("priceAmount")
        @Expose
        val priceAmount: Double = 0.0,

        @SerializedName("totalPrice")
        @Expose
        val totalPrice: String = "",

        @SerializedName("totalPriceAmount")
        @Expose
        val totalPriceAmount: Double = 0.0,

        @SerializedName("extraCharges")
        @Expose
        val extraCharges: ExtraCharges = ExtraCharges(),

        @SerializedName("deals")
        @Expose
        var deals: PriceDeals = PriceDeals()

) {
    data class ExtraCharges(
            @SerializedName("netPrice")
            @Expose
            val netPrice: String = "",

            @SerializedName("netPriceAmount")
            @Expose
            val netPriceAmount: Double = 0.0,

            @SerializedName("extraChargeInfo")
            @Expose
            val extraChargeInfo: List<ExtraChargeInfo> = listOf()

    ) {
        data class ExtraChargeInfo(
                @SerializedName("name")
                @Expose
                val name: String = "",

                @SerializedName("excluded")
                @Expose
                val excluded: Boolean = false,

                @SerializedName("type")
                @Expose
                val type: String = "",

                @SerializedName("price")
                @Expose
                val price: String = "",

                @SerializedName("priceAmount")
                @Expose
                val priceAmount: Double = 0.0

        )
    }

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