package com.tokopedia.product.detail.estimasiongkir.data.model.v3

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RatesModel (
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("rates_id")
        @Expose
        val ratesId: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("texts")
        @Expose
        val texts: RatesTextModel = RatesTextModel(),

        @SerializedName("services")
        @Expose
        val services: List<ServiceModel> = listOf(),

        @SerializedName("recommendations")
        @Expose
        val recommendations: List<Recommendation> = listOf(),

        @SerializedName("info")
        @Expose
        val info: Info = Info(),

        @SerializedName("error")
        @Expose
        val error: Error = Error()
){

    data class RatesTextModel (
        @SerializedName("text_min_price")
        @Expose
        val textMinPrice: String = "",

        @SerializedName("text_destination")
        @Expose
        val textDestination: String = ""
    )
}
