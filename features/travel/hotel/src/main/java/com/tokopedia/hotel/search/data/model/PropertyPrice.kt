package com.tokopedia.hotel.search.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PropertyPrice(
        @SerializedName("price")
        @Expose
        val price: String = "",

        @SerializedName("priceAmount")
        @Expose
        val priceAmount: Float = 0f,

        @SerializedName("totalPrice")
        @Expose
        val totalPrice: String = "",

        @SerializedName("totalPriceAmount")
        @Expose
        val totalPriceAmount: Float = 0f,

        @SerializedName("extraCharges")
        @Expose
        val extraCharges: Charge = Charge()
){

    data class Charge(
            @SerializedName("netPrice")
            @Expose
            val netPrice: String = "",

            @SerializedName("netPriceAmount")
            @Expose
            val netPriceAmount: Float = 0f,

            @SerializedName("extraChargeInfo")
            @Expose
            val extraChargeInfo: List<ChargeInfo> = listOf()
    )

    data class ChargeInfo(
            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("excluded")
            @Expose
            val isExcluded: Boolean = false,

            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("price")
            @Expose
            val price: String = "",

            @SerializedName("priceAmount")
            @Expose
            val priceAmount: Float = 0f
    )
}