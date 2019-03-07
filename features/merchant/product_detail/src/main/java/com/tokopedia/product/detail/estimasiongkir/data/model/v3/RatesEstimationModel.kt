package com.tokopedia.product.detail.estimasiongkir.data.model.v3

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RatesEstimationModel(
        @SerializedName("shop")
        @Expose
        val shop: ShopModel = ShopModel(),

        @SerializedName("address")
        @Expose
        val address: AddressModel = AddressModel(),

        @SerializedName("texts")
        @Expose
        val texts: SummaryText = SummaryText(),

        @SerializedName("rates")
        @Expose
        val rates: RatesModel = RatesModel(),

        @SerializedName("is_blackbox")
        @Expose
        val isBlackbox: Boolean = false
){
    data class Data(
            @SerializedName("data")
            @Expose
            val data: RatesEstimationModel = RatesEstimationModel()
    )

    data class Response (
            @SerializedName("ratesEstimateV3")
            @Expose
            val data: Data = Data()
    )
}