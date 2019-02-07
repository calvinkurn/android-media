package com.tokopedia.product.detail.data.model.estimasiongkir

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RatesModel (
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("texts")
        @Expose
        val texts: RatesTextModel = RatesTextModel(),

        @SerializedName("attributes")
        @Expose
        val attributes: List<ShippingServiceModel> = listOf()){

    data class RatesTextModel (
        @SerializedName("text_min_price")
        @Expose
        val textMinPrice: String = "",

        @SerializedName("text_destination")
        @Expose
        val textDestination: String = ""
    )
}
